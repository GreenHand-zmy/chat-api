package org.peter.chat.web.converter;

import org.peter.chat.enums.BaseStatus;
import org.peter.chat.utils.EnumUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class UniversalEnumConverterFactory implements ConverterFactory<String, BaseStatus> {

    @Override
    public <T extends BaseStatus> Converter<String, T> getConverter(Class<T> aClass) {
        InterStrToEnum converter = new InterStrToEnum(aClass);
        return converter;
    }

    class InterStrToEnum<T extends BaseStatus> implements Converter<String, T> {
        private final Class<T> enumType;

        InterStrToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String s) {
            T t = EnumUtil.codeOf(enumType, Integer.parseInt(s));
            return t;
        }
    }
}
