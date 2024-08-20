package com.example.dmz.data

import com.example.dmz.data.model.Question
import com.example.dmz.data.model.Quiz
import java.time.LocalDate


fun quizList(): List<Quiz> {
    val keywordList = CacheDataSource.getCacheDataSource().getKeywordsList()

    return listOf(
        Quiz(
            date = LocalDate.of(2024, 8, 20),
            keyword = keywordList[0],
            questions = listOf(
                Question(
                    question = "요아정은\n무슨 줄임말일까요?",
                    answers = listOf("요구르트 아줌마 정말 좋아", "요거트 아이스크림의 정석", "요릭 아리 정의의 손길"),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "요아정에서 \"플렉스\"라는 콘텐츠가 유행 중입니다.\n이 \"플렉스\"는 무엇과 관련이 있을까요?",
                    answers = listOf(
                        "많은 토핑을 추가해 가격을 높이는 것",
                        "요거트를 빠르게 먹는 대회",
                        "요아정의 매장을 가장 많이 방문하는 것"
                    ),
                    correctAnswerIndex = 0
                ),
                Question(
                    question = "요아정이 MZ세대 사이에서\n인기를 끌게 된 주된 이유는 무엇인가요?",
                    answers = listOf("저렴한 가격", "커스터마이징 가능한 토핑 선택", "한정된 매장 수"),
                    correctAnswerIndex = 1
                ),
            )
        ),
        Quiz(
            date = LocalDate.of(2024, 8, 21),
            keyword = keywordList[1],
            questions = listOf(
                Question(
                    question = "두바이 초콜릿은 어떤 독특한 재료를 사용해 만든 것으로 유명할까요?",
                    answers = listOf("캐비어", "피스타치오와 카다이프", "트러플 오일"),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "한국에서 두바이 초콜릿이 인기를 끌게 된 이유는 무엇일까요?",
                    answers = listOf(
                        "한국에서만 생산되는 특별한 초콜릿이기 때문에",
                        "SNS와 인플루언서들의 홍보 덕분에",
                        "저렴한 가격 때문에"
                    ),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "두바이 초콜릿은 한국에서 어떤 장소에서 주로 판매되고 있나요?",
                    answers = listOf("고급 레스토랑", "편의점과 백화점 팝업 스토어", "온라인 쇼핑몰 전용"),
                    correctAnswerIndex = 1
                ),
            )
        ),
        Quiz(
            date = LocalDate.of(2024, 8, 22),
            keyword = keywordList[2],
            questions = listOf(
                Question(
                    question = "까먹는 젤리가 인기를 끌고 있는 주요 이유는 무엇일까요?",
                    answers = listOf("젤리가 영양가가 높기 때문에", "독특한 식감과 섭취 방식 때문에", "젤리가 매우 저렴하기 때문에"),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "최근 까먹는 젤리가 논란이 된 이유는 무엇인가요?",
                    answers = listOf("고가의 가격 때문에", "포장지 디자인 문제 때문에", "표시된 성분과 실제 성분이 다르기 때문에"),
                    correctAnswerIndex = 2
                ),
                Question(
                    question = "까먹는 젤리가 특히 인기를 끌고 있는 온라인 플랫폼은 어디인가요?",
                    answers = listOf("네이버 스마트스토어", "쿠팡", "11번가"),
                    correctAnswerIndex = 0
                ),
            )
        ),
        Quiz(
            date = LocalDate.of(2024, 8, 23),
            keyword = keywordList[3],
            questions = listOf(
                Question(
                    question = "쇠일러문은 어떤 뜻의 수식어인가요?",
                    answers = listOf("노쇠한 세일러문", "'쇠맛' 컨셉 쇠+세일러문", "쇠같이 튼튼한 세일러문"),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "쇠일러문은 어떤 그룹에서 유행하기 시작한 밈일까요?",
                    answers = listOf("뉴진스", "아이브", "에스파"),
                    correctAnswerIndex = 2
                ),
                Question(
                    question = "쇠일러문이 지향하는 컨셉은?",
                    answers = listOf("레트로 퓨처리즘", "걸크러쉬", "스팀펑크"),
                    correctAnswerIndex = 0
                ),
            )
        ),
        Quiz(
            date = LocalDate.of(2024, 8, 24),
            keyword = keywordList[3],
            questions = listOf(
                Question(
                    question = "\"원영적 사고\"가 무엇을 의미하나요?",
                    answers = listOf(
                        "실수를 인정하지 않는 사고방식",
                        "항상 긍정적으로 해석하는 사고방식",
                        "모두 비관적으로 바라보는 사고방식"
                    ),
                    correctAnswerIndex = 1
                ),
                Question(
                    question = "\"원영적 사고\"의 원영은 누군가요?",
                    answers = listOf("정원영(정치인)", "최원영(야구선수)", "장원영(가수)"),
                    correctAnswerIndex = 2
                ),
                Question(
                    question = "\"원영적 사고\" 에서 유래된 유행어는?",
                    answers = listOf("완전 럭키비키잖아~!", "오히려 좋아", "정신승리"),
                    correctAnswerIndex = 0
                ),
            )
        ),
    )
}