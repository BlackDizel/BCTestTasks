using System.Windows;
using System.Windows.Controls;
using WpfTTWeather.Classes;

namespace WpfTTWeather.Controls
{
    public partial class weatherItem : UserControl
    {
        internal WeatherData wd;
        int index=-1;
        public string Day
        {
            get { return tbDate.Text;  }
            set { tbDate.Text = value; }
        }
        
        public string Temperature
        {
            get { return tbTemperature.Text;  }
            set { tbTemperature.Text = value; }
        }

    
        public weatherItem()
        {
            InitializeComponent();
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
                if (index == -1)
                    for (int i = 0; i < wd.wi.Length; ++i)
                        if (wd.wi[i].Date == tbDate.Text)
                            index = i;
                wd.EditData(index, sender as TextBox);
            }
        }
    }
}
