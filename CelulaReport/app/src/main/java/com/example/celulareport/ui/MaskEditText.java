package com.example.celulareport.ui;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;

public abstract class MaskEditText {

    public static final String FORMAT_CPF = "###.###.###-##";
    public static final String FORMAT_FONE = "(###)####-#####";
    public static final String FORMAT_CEP = "#####-###";
    public static final String FORMAT_DATE = "##/##/####";
    public static final String FORMAT_HOUR = "##:##";
    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return TextWatcher according to the mask
     */
    public static TextWatcher mask(final TextInputEditText ediTxt, final String mask) {
        return new TextWatcher() {
            //get format number in a context country
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            //Verify if the current text already is update
            boolean isUpdating;
            String oldTex = "";

            @Override
            public void afterTextChanged(final Editable s) { }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String currentText = MaskEditText.unmask(s.toString());
                if (isUpdating) {
                    oldTex = currentText;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                String mascara="";
                for (final char m : mask.toCharArray()) {

                    //&& currentText.length() > oldTex.length()
                    if (m!= '#') {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += currentText.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    /**
     * This method to be called to use money mask in editTex
     *
     * @param ediTxt
     * @return TextWatcher money mask
     */
    public static TextWatcher mask(final TextInputEditText ediTxt) {
        //get format number in a context country
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        return new TextWatcher() {

            //Verify if the current text already is update
            boolean isUpdating;
            String oldTex = "";

            @Override
            public void afterTextChanged(final Editable s) { }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }

            @Override
            public void onTextChanged(CharSequence s, final int start, final int before, final int count) {

                String currentText = s.toString();
                currentText = MaskEditText.unmask(currentText);

                if (isUpdating) {
                    oldTex = currentText;
                    isUpdating = false;
                    return;
                }
                String mascara = "";

                try {

                    mascara = numberFormat.format(Double.parseDouble(currentText)/100);
                    //Use two decimal number to show zero right in decimal and avoid truncate when type 0
                    numberFormat.setMinimumFractionDigits(2);

                } catch (final NumberFormatException e) {
                    s = "";
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }
        };
    }

    public static String unmask(final String s) {
        return s.replaceAll("[^\\d]", "");
        //return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }
}
