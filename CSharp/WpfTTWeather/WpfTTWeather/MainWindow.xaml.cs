using System;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Xml.Linq;
using WpfTTWeather.Classes;
using WpfTTWeather.Controls;

namespace WpfTTWeather
{

    public partial class MainWindow : Window
    {
        WebClient wc;
        WeatherData wd;
        public MainWindow()
        {
            InitializeComponent();
            wc = new WebClient();
            wc.DownloadStringCompleted += wc_DownloadStringCompleted;
            wd = new WeatherData();
            UpdateUI();


        }

        /// <summary>
        /// по нажатию на кнопку пытаемся обновиться данными с сервера
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void btUpdateClick(object sender, RoutedEventArgs e)
        {
            wc.DownloadStringAsync(new Uri("http://api.openweathermap.org/data/2.5/forecast/daily?q=Moscow&mode=xml&units=metric&cnt=7", UriKind.Absolute));
        }

        /// <summary>
        /// обработка события завершения попытки скачать данные с сервера
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void wc_DownloadStringCompleted(object sender, DownloadStringCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                //парсим данные с сервера
                XDocument d = XDocument.Parse(e.Result);
                var dt =
                from c in d.Descendants("time")
                select new WeatherInfo() 
                {
                    Date = c.Attribute("day").Value.ToString(),
                    Temperature = c.Element("temperature").Attribute("day").Value.ToString()
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

                //обновляем UI
                UpdateUI();
            }
        }

        /// <summary>
        /// обновление значений в текстовых полях температуры
        /// </summary>
        void UpdateUI()
        {
            if (wd.wi[0] != null)
            {
                tbToday.Text = wd.wi[0].IsUserMode ? wd.wi[0].UserValue : wd.wi[0].Temperature;
                int i = 0;
                foreach (var el in wpDays.Children)
                {
                    ++i;
                    var item = el as weatherItem;
                    if (item.wd == null) item.wd = wd;
                    item.Day = wd.wi[i].Date;
                    item.Temperature = wd.wi[i].IsUserMode ? wd.wi[i].UserValue : wd.wi[i].Temperature;

                }
            }
            else
                btUpdateClick(null, null);
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
    }
}
