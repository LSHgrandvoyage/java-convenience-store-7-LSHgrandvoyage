package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MethodTest extends NsTest{
    @Test
    public void testWelcome() {
        InputView.welcome();
        assertThat(output()).contains(
                "안녕하세요. W편의점입니다.\n" +
                "현재 보유하고 있는 상품입니다.\n" +
                "\n" +
                "- 콜라 1,000원 10개 탄산2+1\n" +
                "- 콜라 1,000원 10개\n" +
                "- 사이다 1,000원 8개 탄산2+1\n" +
                "- 사이다 1,000원 7개\n" +
                "- 오렌지주스 1,800원 9개 MD추천상품\n" +
                "- 오렌지주스 1,800원 재고 없음\n" +
                "- 탄산수 1,200원 5개 탄산2+1\n" +
                "- 탄산수 1,200원 재고 없음\n" +
                "- 물 500원 10개\n" +
                "- 비타민워터 1,500원 6개\n" +
                "- 감자칩 1,500원 5개 반짝할인\n" +
                "- 감자칩 1,500원 5개\n" +
                "- 초코바 1,200원 5개 MD추천상품\n" +
                "- 초코바 1,200원 5개\n" +
                "- 에너지바 2,000원 5개\n" +
                "- 정식도시락 6,400원 8개\n" +
                "- 컵라면 1,700원 1개 MD추천상품\n" +
                "- 컵라면 1,700원 10개");
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
