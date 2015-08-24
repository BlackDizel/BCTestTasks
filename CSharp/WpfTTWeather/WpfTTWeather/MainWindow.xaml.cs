using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media.Animation;
using System.Xml.Linq;
using WpfTTWeather.Classes;
using WpfTTWeather.Controls;

namespace WpfTTWeather
{

    public partial class MainWindow : Window
    {
        WebClient wc;
        WeatherData wd;
        AnimationQueue aq;
        public MainWindow()
        {
            InitializeComponent();
            
            wc = new WebClient();
            wc.DownloadStringCompleted += wc_DownloadStringCompleted;
            wd = new WeatherData();

            aq = new AnimationQueue(new List<Storyboard>() {(Storyboard)this.Resources["In"],(Storyboard)this.Resources["Out"] });
            UpdateUI();

        }

        


        /// <summary>
        /// по нажатию на кнопку пытаемся обновиться данными с сервера
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btUpdateClick(object sender, RoutedEventArgs e)
        {
            aq.AddAnimation(0);
            try
            {

                wc.DownloadStringAsync(new Uri("http://api.openweathermap.org/data/2.5/forecast/daily?q=Moscow&mode=xml&units=metric&cnt=7", UriKind.Absolute));
            }
            catch
            {
                Debug.WriteLine("download error");

                tbError.Visibility = Visibility.Visible;
                UpdateUI();
            
            }
        }

        /// <summary>
        /// обработка события завершения попытки скачать данные с сервера
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void wc_DownloadStringCompleted(object sender, DownloadStringCompletedEventArgs e)
        {
            tbError.Visibility = Visibility.Collapsed;

            if (e.Error == null)
            {

                //парсим данные с сервера
                XDocument d = XDocument.Parse(e.Result);
                var dt =
                from c in d.Descendants("time")
                select new WeatherInfo()
                {
                    Date = c.Attribute("day").Value.ToString(),
                    Temperature = c.Element("temperature").Attribute("day").Value.ToString()+"° C"
                };

                //сдвиг массива данных если прошло больше 1 дня после последнего обновления 
                wd.NormalizeAtDate(dt.ElementAt(0).Date);

                //обогощаем данные данными с сервера
                for (int i = 0; i < dt.Count(); ++i)
                    if (wd.wi[i] != null)
                    {
                        if (wd.wi[i].IsUserMode)
                            wd.wi[i].Temperature = dt.ElementAt(i).Temperature;
                        else
                            wd.wi[i] = dt.ElementAt(i);
                    }
                    else wd.wi[i] = dt.ElementAt(i);

                //сохраняем данные в локальное хранилище
                wd.Save();
            }
            else
            {
                tbError.Visibility = Visibility.Visible;
            }
            //обновляем UI
            UpdateUI();
        }

        /// <summary>
        /// обновление значений в текстовых полях температуры
        /// </summary>
        void UpdateUI()
        {
            aq.AddAnimation(1);

            if (wd.wi[0] != null)
            {
                grContainerData.Visibility = Visibility.Visible;

                tbToday.Text = wd.wi[0].IsUserMode ? wd.wi[0].UserValue : wd.wi[0].Temperature;
                int i = 0;
                foreach (var el in wpDays.Children)
                {
                    ++i;
                    var item = el as weatherItem;
                    if (item.wd == null) item.wd = wd;
                    item.Day = i != 1 ? DateTime.Parse(wd.wi[i].Date).ToString("dd MMMM") : "завтра";
                    item.Temperature = wd.wi[i].IsUserMode ? wd.wi[i].UserValue : wd.wi[i].Temperature;

                }
            }
            else tbError.Visibility = Visibility.Visible;
        }

        /// <summary>
        /// обработка события потери фокуса окном температуры
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void tbToday_LostFocus(object sender, RoutedEventArgs e)
        {
            if (wd != null)
            {
                wd.EditData(0, sender as TextBox);            
            }

        }

        private void tbToday_KeyUp(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
                tbToday_LostFocus(sender, null);
            
        }


    }
}
