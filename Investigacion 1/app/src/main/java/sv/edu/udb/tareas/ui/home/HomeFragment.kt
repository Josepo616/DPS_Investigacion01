package sv.edu.udb.tareas.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import sv.edu.udb.tareas.R
import sv.edu.udb.tareas.databinding.FragmentHomeBinding
import android.text.Editable
import android.text.TextWatcher

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener { view ->
            val dialogView = LayoutInflater.from(context).inflate(R.layout.new_item_dialog, null)

            val taskEditText = dialogView.findViewById<EditText>(R.id.taskEditText)
            val desc = dialogView.findViewById<EditText>(R.id.desc)
            val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
            val addButton = dialogView.findViewById<Button>(R.id.addButton)

            addButton.isEnabled = false  // Deshabilitar el botón al inicio

            // Agregar TextWatcher para habilitar el botón cuando hay texto en taskEditText
            taskEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    addButton.isEnabled = s.toString().trim().isNotEmpty()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            val dialog = context?.let { Dialog(it) }
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(dialogView)
                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
                addButton.setOnClickListener {
                    if (context?.let { it1 ->
                            homeViewModel.addTask(
                                it1,
                                binding.recyclerViewTasks,
                                taskEditText.text.toString(),
                                desc.text.toString(),
                                0L,
                                0,
                                0L
                            )
                        } == true) {
                        dialog.dismiss()
                        val snackbar = Snackbar.make(view, "Item Added", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    } else {
                        val snackbar = Snackbar.make(
                            view,
                            "Couldn't add item, try again",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.show()
                    }
                }
                val dialogHeight = context?.let { dpToPx(450, it) }
                val dialogWidth = context?.let { dpToPx(400, it) }
                if (dialogWidth != null && dialogHeight != null) {
                    dialog.window?.setLayout(dialogWidth, dialogHeight)
                }
                dialog.show()
            }
        }

        homeViewModel.getTasksToRecycler(requireContext(), binding.recyclerViewTasks)
        return binding.root
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
