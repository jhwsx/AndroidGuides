package com.example.navigationexample

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
fun getQuestionList(): List<Question> {
    return listOf(
        Question(
            text = "下面哪位是中国历史上在位时间最长的皇帝？",
            choices = listOf("康熙皇帝", "乾隆皇帝", "汉武帝", "万历皇帝"),
            answer = "康熙皇帝"
        ),
        Question(
            text = "下面哪位是中国历史上在位时间最短的皇帝？",
            choices = listOf("金末帝","汉废帝","唐殇帝","明光宗"),
            answer = "金末帝"
        ),
        Question(
            text = "秦朝末年，陈胜吴广是在哪个地方起义的？",
            choices = listOf("大泽乡","新乡","渔阳","淮阳"),
            answer = "大泽乡"
        ),
        Question(
            text = "中国唯一以“县”和“市”为名的县级市是下边哪个？",
            choices = listOf("辉县","林县","郏县","禹县"),
            answer = "辉县"
        ),
        Question(
            text = "中国历史上时间最长的朝代是下面哪个朝代？",
            choices = listOf("周朝","夏朝","商朝","汉朝"),
            answer = "周朝"
        ),
        Question(
            text = "下面哪一位是中国历史上的农民皇帝？",
            choices = listOf("刘邦","刘备","朱元璋","李自成"),
            answer = "朱元璋"
        )
    )
}