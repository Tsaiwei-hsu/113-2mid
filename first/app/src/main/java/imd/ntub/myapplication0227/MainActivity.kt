package imd.ntub.myapplication0227

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import imd.ntub.myapplication0227.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**  binding和txt只能選一個
        setContentView(R.layout.activity_main)

        val txtScissors: TextView = findViewById(R.id.txtScissors)
        val txtRock = findViewById<TextView>(R.id.txtRock)
        val txtPaper: TextView = findViewById<TextView>(R.id.txtPaper)
        val txtResult: TextView = findViewById<TextView>(R.id.txtResult)
        val rbtnScissors = findViewById<RadioButton>(R.id.rbtnScissors)
        val rbtnRock = findViewById<RadioButton>(R.id.rbtnRock)
        val rbtnPaper = findViewById<RadioButton>(R.id.rbtnPaper)
        val rdbguess = findViewById<RadioGroup>(R.id.rdgGuess)
        val btnGuess = findViewById<Button>(R.id.btnGuess)
         */
        //1.剪刀2.石頭3.布
        binding.btnGuess.setOnClickListener {
            /*txtResult.text = when(rdbguess.checkedRadioButtonId){
                R.id.rbtnPaper->"你按布"
                R.id.rbtnScissors->"你按剪刀"
                R.id.rbtnRock->"你按石頭"
            else->"沒選東西"
            }*/
            val random = (1..3).random()  //<=不用自己輸;知道電腦選甚麼
            when(random){
                1 -> {
                    binding.txtScissors.setBackgroundResource(R.drawable.bg_guess)
                    binding.txtRock.setBackgroundResource(0)
                    binding.txtPaper.setBackgroundResource(0)
                }

                2 -> {
                    binding.txtScissors.setBackgroundResource(0)
                    binding.txtRock.setBackgroundResource(R.drawable.bg_guess)
                    binding.txtPaper.setBackgroundResource(0)
                }

                3 -> {
                    binding.txtScissors.setBackgroundResource(0)
                    binding.txtRock.setBackgroundResource(0)
                    binding.txtPaper.setBackgroundResource(R.drawable.bg_guess)
                }
            }
            when(binding.rdgGuess.checkedRadioButtonId){
                R.id.rbtnScissors->{           //這個是我們選的
                    when (random) {
                        1 -> binding.txtResult.text = "電腦出剪刀-平手"
                        2 -> binding.txtResult.text = "電腦出石頭-你輸了"
                        3 -> binding.txtResult.text = "電腦出布-你贏了"
                    }
                }
                R.id.rbtnPaper->{
                    when (random) {
                        1 -> binding.txtResult.text = "電腦出剪刀-你輸了"
                        2 -> binding.txtResult.text = "電腦出石頭-你贏了"
                        3 -> binding.txtResult.text = "電腦出布-平手"
                    }
                }
                R.id.rbtnRock->{
                    when (random) {
                        1 -> binding.txtResult.text = "電腦出剪刀-你贏了"
                        2 -> binding.txtResult.text = "電腦出石頭-平手"
                        3 -> binding.txtResult.text = "電腦出布-你輸了"
                    }
                }
                else->{
                    binding.txtResult.text = "沒有選東西"
                }
            }
        }
    }
}
