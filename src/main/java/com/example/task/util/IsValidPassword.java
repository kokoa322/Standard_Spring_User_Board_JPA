package com.example.task.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsValidPassword {
    public static String isValidPassword(String password) {
// 최소 8자, 최대 20자 상수 선언
        final int MIN = 4;
        final int MAX = 20;

// 영어, 숫자, 특수문자 포함한 MIN to MAX 글자 정규식
        final String REGEX =
                "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
// 3자리 연속 문자 정규식
        final String SAMEPT = "(\\w)\\1\\1";
// 공백 문자 정규식
        final String BLANKPT = "(\\s)";

        // 정규식 검사객체
        Matcher matcher;

        // 공백 체크
        if (password == null || "".equals(password)) {
            return "Detected: No Password";
        }

        // ASCII 문자 비교를 위한 UpperCase
        String tmpPw = password.toUpperCase();
        // 문자열 길이
        int strLen = tmpPw.length();

        // 글자 길이 체크
        if (strLen > MAX || strLen < MIN) {
            return "Detected: Incorrect Length(Length: " + strLen + ")";
        }

        // 공백 체크
        matcher = Pattern.compile(BLANKPT).matcher(tmpPw);
        if (matcher.find()) {
            return "Detected: Blank";
        }

        // 비밀번호 정규식 체크
        matcher = Pattern.compile(REGEX).matcher(tmpPw);
        if (!matcher.find()) {
            return "Detected: Wrong Regex";
        }

        // 동일한 문자 3개 이상 체크
        matcher = Pattern.compile(SAMEPT).matcher(tmpPw);
        if (matcher.find()) {
            return "Detected: Same Word";
        }

        // 연속된 문자 / 숫자 3개 이상 체크
        if (matcher.find()){
            // ASCII Char를 담을 배열 선언
            int[] tmpArray = new int[strLen];

            // Make Array
            for (int i = 0; i < strLen; i++) {
                tmpArray[i] = tmpPw.charAt(i);
            }

            // Validation Array
            for (int i = 0; i < strLen - 2; i++) {
                // 첫 글자 A-Z / 0-9
                if ((tmpArray[i] > 47
                        && tmpArray[i + 2] < 58)
                        || (tmpArray[i] > 64
                        && tmpArray[i + 2] < 91)) {
                    // 배열의 연속된 수 검사
                    // 3번째 글자 - 2번째 글자 = 1, 3번째 글자 - 1번째 글자 = 2
                    if (Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1
                            && Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2) {
                        char c1 = (char) tmpArray[i];
                        char c2 = (char) tmpArray[i + 1];
                        char c3 = (char) tmpArray[i + 2];
                        return "Detected: Continuous Pattern: \"" + c1 + c2 + c3 + "\"";
                    }
                }
            }
            // Validation Complete
            return ">>> All Pass";
        }
        return "사용가능한 비밀번호 입니다.";
    }
}
