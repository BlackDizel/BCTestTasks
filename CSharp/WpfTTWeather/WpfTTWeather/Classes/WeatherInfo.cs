using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WpfTTWeather.Classes
{
    public class WeatherInfo
    {
        public bool IsUserMode;
        public string Temperature;
        public string UserValue;
        public string Date;

        public WeatherInfo()
        {
            IsUserMode = false;
            UserValue = "";
        }
    }
}
