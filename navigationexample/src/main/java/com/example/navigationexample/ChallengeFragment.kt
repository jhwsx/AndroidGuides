package com.example.navigationexample

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationexample.databinding.ChallengeFragmentBinding
import timber.log.Timber

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
class ChallengeFragment : Fragment() {
    private val questions: MutableList<Question> = getQuestionList().toMutableList()
    private lateinit var binding: ChallengeFragmentBinding
    private var questionIndex: Int = 0
    private val questionNums: Int = Math.min((questions.size + 1) / 2, 3)
    private lateinit var currentChoices: MutableList<String>
    private lateinit var currentQuestion: Question
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChallengeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        randomizeQuestions()
        setCurrentQuestion()
        setupQuestionText()
        setupQuestionChoices()
        setupAppBarTitle()
        binding.btnSubmit.setOnClickListener {
            val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId
            if (checkedRadioButtonId != -1) {
                var checkedRadioButton = when (checkedRadioButtonId) {
                    binding.radioButton1.id -> binding.radioButton1
                    binding.radioButton2.id -> binding.radioButton2
                    binding.radioButton3.id -> binding.radioButton3
                    binding.radioButton4.id -> binding.radioButton4
                    else -> binding.radioButton1
                }
                if (TextUtils.equals(checkedRadioButton.text, currentQuestion.answer)) {
                    questionIndex++
                    // 答案正确
                    if (questionIndex < questionNums) {
                        // 继续下一题
                        Timber.d("next chanllenge")
                        setCurrentQuestion()
                        setupQuestionText()
                        setupQuestionChoices()
                        setupAppBarTitle()
                    } else {
                        // 挑战成功
                        Timber.d("chanllenge success")
                        findNavController().navigate(R.id.action_challengeFragment_to_challengeSuccessFragment)
                    }
                } else {
                    // 答案错误，挑战结束
                    Timber.d("chanllenge fail")
                    findNavController().navigate(R.id.action_challengeFragment_to_challengeFailFragment)
                }
            }
        }
    }

    private fun setupAppBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_history_chanllenge_question, questionIndex + 1, questionNums)
    }

    private fun setupQuestionChoices() {
        binding.radioButton1.text = currentChoices[0]
        binding.radioButton2.text = currentChoices[1]
        binding.radioButton3.text = currentChoices[2]
        binding.radioButton4.text = currentChoices[3]
        binding.radioButton1.isChecked = true
    }

    private fun setupQuestionText() {
        binding.tvQuestion.text = currentQuestion.text
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
    }

    private fun setCurrentQuestion() {
        currentQuestion = questions[questionIndex]
        currentChoices = currentQuestion.choices.toMutableList()
        currentChoices.shuffle()
    }
}