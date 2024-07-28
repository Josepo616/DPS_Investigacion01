package sv.edu.udb.tareas


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import sv.edu.udb.tareas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // No hace nada en cada tick
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, ViewTasksActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}
