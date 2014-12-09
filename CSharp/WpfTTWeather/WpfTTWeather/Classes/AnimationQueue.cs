using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Animation;

namespace WpfTTWeather.Classes
{

    class AnimationQueue
    {
        List<int> lAnim;
        List<Storyboard> Animations;
        public AnimationQueue(List<Storyboard> anims)
        {
            lAnim = new List<int>();
            Animations = anims;
            foreach (var el in anims)
                el.Completed += anim_completed;
        }

        private void anim_completed(object sender, EventArgs e)
        {
            if (lAnim.Count > 0) lAnim.RemoveAt(0);
            if (lAnim.Count > 0)
                Animations[lAnim[0]].Begin();            
        }
        public void AddAnimation(int index)
        {
            lAnim.Add(index);
            if (lAnim.Count == 1)
                Animations[index].Begin();
        }
    }
}
