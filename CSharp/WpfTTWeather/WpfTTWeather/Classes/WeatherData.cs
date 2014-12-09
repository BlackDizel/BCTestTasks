using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Xml.Serialization;

namespace WpfTTWeather.Classes
{
    class WeatherData
    {
        public WeatherInfo[] wi;
        public WeatherData()
        {
            Load();
            
        }

        /// <summary>
        /// загрузка данных из файла
        /// </summary>
        void Load()
        {
            if (File.Exists("data.xml"))
            {
                using (var stream = File.OpenRead("data.xml"))
                {
                    var serializer = new XmlSerializer(typeof(WeatherInfo[]));
                    wi = (WeatherInfo[])serializer.Deserialize(stream);
                }
            }
            else wi= new WeatherInfo[7];
 
        }
        /// <summary>
        /// сохранение данных в локальный файл
        /// </summary>
        public void Save()
        {
            using (var stream = File.Create("data.xml"))
            {
                var serializer = new XmlSerializer(typeof(WeatherInfo[]));
                serializer.Serialize(stream, wi);
            }
        }

        /// <summary>
        /// обновление данных при изменении текущей даты
        /// </summary>
        /// <param name="date"></param>
        public void NormalizeAtDate(string date)
        {
            if (wi[0] == null) return;
            for (int i = 0; i < wi.Length; ++i)
            {
                if (wi[0].Date == date) return;
                else
                {
                    int j;
                    for (j=0; j < wi.Length-1; ++j)
                        wi[j] = wi[j + 1];
                    wi[j] = new WeatherInfo();
                }
            }
            Save();

        }
        
        public void EditData(int index, TextBox tb)
        {
            var el = wi[index];
            el.IsUserMode = false;

            if (tb.Text == "")
                tb.Text = el.Temperature;
            else
            if (el.Temperature != tb.Text)
            {
                el.UserValue = tb.Text;
                el.IsUserMode = true;
            } 

            Save();
        }
    }
}
