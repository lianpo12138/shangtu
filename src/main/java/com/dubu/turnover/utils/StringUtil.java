package com.dubu.turnover.utils;



import org.apache.commons.lang3.StringUtils;

import com.vdurmont.emoji.EmojiParser;


public class StringUtil {
    /**
     * emoji 表情encode 转码
     */
    public static String encodeEmoji(String content) {
        if (StringUtils.isNotEmpty(content)) {
            EmojiParser.EmojiTransformer transformer = unicodeCandidate -> unicodeCandidate.getEmoji().getHtmlHexadecimal();
            return EmojiParser.parseFromUnicode(content, transformer);
        }
        return null;
    }

    /**
     * emoji表情解码
     */
    public static String decodeEmoji(String content) {
        if (StringUtils.isNotEmpty(content)) {
            return EmojiParser.parseToUnicode(content);
        }
        return null;
    }
    
}