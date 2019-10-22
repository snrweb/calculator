package com.example.javaproject;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    private EditText textInput;
    private String currentContent;
    private TextView result;
    private int openBracketCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.textInput);
        textInput.setShowSoftInputOnFocus(false);

        Button celciusBtn = findViewById(R.id.buttonC);
        Button farenheitBtn = findViewById(R.id.buttonF);
        result = findViewById(R.id.resultid);

        celciusBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentContent.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Value is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                convertToCelsius();
            }
        });

        farenheitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentContent.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Value is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                convertToFahrenheit();
            }
        });

    }

    public void numOneBtn(View view) {
        setEditTextContent("1");
    }

    public void numTwoBtn(View view) {
        setEditTextContent("2");
    }

    public void numThreeBtn(View view) {
        setEditTextContent("3");
    }

    public void numFourBtn(View view) {
        setEditTextContent("4");
    }

    public void numFiveBtn(View view) {
        setEditTextContent("5");
    }

    public void numSixBtn(View view) {
        setEditTextContent("6");
    }

    public void numSevenBtn(View view) {
        setEditTextContent("7");
    }

    public void numEightBtn(View view) {
        setEditTextContent("8");
    }

    public void numNineBtn(View view) {
        setEditTextContent("9");
    }

    public void numZeroBtn(View view) {
        setEditTextContent("0");
    }

    public void signPlusBtn(View view) {
        setEditTextContent("+");
    }

    public void signMinusBtn(View view) {
        setEditTextContent("-");
    }

    public void signMultiplicationsBtn(View view) {
        setEditTextContent("x");
    }

    public void signDivisionBtn(View view) {
        setEditTextContent("/");
    }

    public void openBracketBtn(View view) {
        setEditTextContent("(");
    }

    public void closeBracketBtn(View view) {
        setEditTextContent(")");
    }

    public void decimalPointBtn(View view) {
        setEditTextContent(".");
    }

    public void signEqualBtn(View view) {
        if(openBracketCount == 0) {
            try{
                calculate();
                ArrayList<String> contentList = new ArrayList<>(Arrays.asList(currentContent.split("[.]")));
                while (contentList.contains("")) {
                    contentList.remove(contentList.indexOf(""));
                }

                String result;
                if(contentList.size()==2 && contentList.get(1).equalsIgnoreCase("0")) {
                    result = contentList.get(0);
                    currentContent = result;
                } else {
                    result = currentContent;
                }
                this.result.setText(result);
            } catch (Exception e) {
                this.result.setText("Error");
            }
        } else {
            this.result.setText("Error");
        }
    }

    public void cancelPositionedInput(View v) {
        ArrayList<String> currentContentList = new ArrayList<>(Arrays.asList(currentContent.split("")));
        int cursorPosition = textInput.getSelectionStart();

        if(currentContentList.size() > 1 && result.getText().toString().isEmpty()) {
            currentContentList.remove(cursorPosition);
            String content = TextUtils.join("", currentContentList);

            textInput.setText(content);
            textInput.setSelection(cursorPosition - 1);

            currentContent = textInput.getText().toString();
        }
    }

    public void cancelAllInput(View v) {
        if(currentContent != null) {
            currentContent = "";
            textInput.setText("");
            result.setText("");
        }
    }

    private boolean isError(String newString) {
        ArrayList<String> currentContentList = new ArrayList<>();

        if(currentContent != null){
            currentContentList = new ArrayList<>(Arrays.asList(currentContent.split("")));
        }

        if(newString.endsWith("-")) {
            if(currentContentList.size() > 0 && (currentContentList.get(textInput.getSelectionStart()).endsWith("+")
                    || currentContentList.get(textInput.getSelectionStart()).endsWith("-"))) {
                return false;
            }
        }

        if (currentContent == null && (newString.endsWith("x") || newString.endsWith("+")
                || newString.endsWith("/") || newString.endsWith("."))) {
            return false;
        }

        if (newString.endsWith("x") || newString.endsWith("+") || newString.endsWith("/") || newString.endsWith(".")) {
            if (currentContent == null || currentContentList.get(textInput.getSelectionStart()).endsWith("x")
                    || currentContentList.get(textInput.getSelectionStart()).endsWith("/")
                    || currentContentList.get(textInput.getSelectionStart()).endsWith("+")
                    || currentContentList.get(textInput.getSelectionStart()).endsWith("-")
                    || currentContentList.get(textInput.getSelectionStart()).endsWith("(")) {
                return false;
            }
        }

        if(newString.endsWith(")")) {
            if (currentContent == null) {
                return false;
            }
        }
        return true;
    }

    private void setEditTextContent(String newString) {
        if(result.getText().toString().isEmpty()) {
            if (!isError(newString)) return;
        }

        if(newString.endsWith("(")) openBracketCount += 1;
        if(newString.endsWith(")")) openBracketCount -= 1;

        if(currentContent == null) {
            textInput.setText(newString);
            textInput.setSelection(1);
        } else {
            int cursorPosition = textInput.getSelectionStart();
            ArrayList<String> contentList = new ArrayList<>(Arrays.asList(currentContent.split("")));

            if(result.getText().toString().isEmpty()) {
                contentList.add(cursorPosition+1, newString);
            } else {
                contentList.add(currentContent.length()+1, newString);
            }

            String content = TextUtils.join("", contentList);

            textInput.setText(content);
            textInput.setSelection(currentContent.length() + 1);
        }
        result.setText("");
        currentContent = textInput.getText().toString();
    }

    public void calculate() {
        if(currentContent.length() > currentContent.replace("(", "").length()) {
            operationsWithBracket();
        }

        if(currentContent.length() > currentContent.replace("/", "").length()) {
            currentContent = divide(currentContent);
        }

        if(currentContent.length() > currentContent.replace("x", "").length()) {
            multiply(currentContent);
        }

        if(currentContent.length() > currentContent.replace("+", "").length() ||
                currentContent.length() > currentContent.replace("-", "").length()) {
            currentContent = String.valueOf(add(currentContent));
        }
    }

    public String sideCalculate(String newString) {
        if(newString.length() > newString.replace("/", "").length()) {
            newString = divide(newString);
        }

        if(newString.length() > newString.replace("x", "").length()) {
            newString = multiply(newString);
        }

        if(newString.length() > newString.replace("+", "").length() ||
                newString.length() > newString.replace("-", "").length()) {
            newString = String.valueOf(add(newString));
        }
        return newString;
    }

    private void operationsWithBracket() {
        ArrayList<String> contentAry = new ArrayList<>(Arrays.asList(currentContent.split("")));
        int countContentAry = contentAry.size();
        for(int i=0; i<countContentAry; i++) {
            if(contentAry.get(i).startsWith("(") && i>0) {
                if(!contentAry.get(i-1).isEmpty() && !contentAry.get(i-1).endsWith("x") && !contentAry.get(i-1).endsWith("/")) {
                    if(contentAry.get(i-1).endsWith("+") || contentAry.get(i-1).endsWith("-")) {
                        contentAry.set(i, "1x" + contentAry.get(i));
                    } else {
                        contentAry.set(i, "x" + contentAry.get(i));
                    }
                }
            }

            if(contentAry.get(i).startsWith(")") && i<countContentAry-1) {
                if(!contentAry.get(i+1).isEmpty() && !contentAry.get(i+1).endsWith("x")
                        && !contentAry.get(i+1).endsWith("/") && !contentAry.get(i+1).endsWith("+")
                        && !contentAry.get(i+1).endsWith("-") && !contentAry.get(i+1).endsWith(")")) {
                    contentAry.set(i, ")x");
                }
            }
        }

        currentContent = TextUtils.join("", contentAry);
        ArrayList<String> currentAry = new ArrayList<>(Arrays.asList(currentContent.split("")));
        while(currentAry.contains("")) {
            currentAry.remove("");
        }

        ArrayList<String> operandAry = new ArrayList<>();
        ArrayList<String> bracket = new ArrayList<>();
        int bracketIsOpen = 0;
        int countCurrentAry = currentAry.size();

        for(int i=0; i<countCurrentAry; i++) {
            if(currentAry.get(i).endsWith("(")) {
                bracketIsOpen += 1;
                continue;
            }

            if(currentAry.get(i).endsWith(")")) {
                bracketIsOpen -= 1;
                if(!bracket.get(bracket.size()-1).endsWith("x") && !bracket.get(bracket.size()-1).endsWith("/")) {
                    String result = sideCalculate(bracket.get(bracket.size()-1));
                    if(bracket.size() == 1) {
                        operandAry.add(result);
                    } else {
                        bracket.set(bracket.size()-2, bracket.get(bracket.size()-2) + result);
                    }
                    bracket.remove(bracket.size()-1);
                }

                if(bracketIsOpen == 0 && bracket.size() > 0) {
                    String newJoin = TextUtils.join("", bracket);
                    newJoin = sideCalculate(newJoin);
                    operandAry.add(newJoin);
                    bracket.removeAll(bracket);
                }
                continue;
            }
            if(bracketIsOpen > 0 && currentAry.get(i-1).endsWith("(")) {
                bracket.add(currentAry.get(i));
                continue;
            }
            if(bracketIsOpen > 0 && !currentAry.get(i-1).endsWith("(")) {
                bracket.set(bracket.size()-1, bracket.get(bracket.size()-1) + currentAry.get(i));
                continue;
            }

            if(bracketIsOpen == 0) {
                operandAry.add(currentAry.get(i));
            }
        }

        currentContent = TextUtils.join("", operandAry);
    }

    private String multiply(String numberStrings) {
        if(!numberStrings.startsWith("-") && !numberStrings.startsWith("+")) numberStrings = "+" + numberStrings;

        ArrayList<String> numberStringList = new ArrayList<>(Arrays.asList(numberStrings.split("")));
        ArrayList<String> multiplicationList = new ArrayList<>();

        int numberStringListCount = numberStringList.size();
        for(int i=0; i<numberStringListCount; i++) {
            if(numberStringList.get(i).isEmpty()) continue;
            if(numberStringList.get(i).startsWith("+") || numberStringList.get(i).startsWith("-")
                    || numberStringList.get(i).startsWith("x")) {
                multiplicationList.add(numberStringList.get(i));
            }

            int index = multiplicationList.size()-1;
            if(!numberStringList.get(i).startsWith("+") && !numberStringList.get(i).startsWith("-")
                    && !numberStringList.get(i).startsWith("x")) {
                if(!multiplicationList.get(index).startsWith("x")) {
                    multiplicationList.set(index, multiplicationList.get(index) + numberStringList.get(i));
                }
                if(multiplicationList.get(index).startsWith("x")) {
                    multiplicationList.add(numberStringList.get(i));
                }
            }
        }

        for(int i=multiplicationList.size()-1; i>0; i--) {
            if(multiplicationList.get(i).startsWith("x")) {
                Double operandA = Double.parseDouble(multiplicationList.get(i-1));
                Double operandB = Double.parseDouble(multiplicationList.get(i+1));
                Double result = operandA * operandB;
                if(result > 0) {
                    multiplicationList.set(i-1, "+" + String.valueOf(result));
                } else {
                    multiplicationList.set(i-1, String.valueOf(result));
                }
                multiplicationList.remove(i+1);
                multiplicationList.remove(i);
            }
        }

        currentContent = TextUtils.join("", multiplicationList);
        return currentContent;
    }

    private String divide(String numberStrings) {
        if(!numberStrings.startsWith("-") && !numberStrings.startsWith("+")) numberStrings = "+" + numberStrings;

        ArrayList<String> numberStringList = new ArrayList<>(Arrays.asList(numberStrings.split("")));
        ArrayList<String> divisionList = new ArrayList<>();

        int numberStringListCount = numberStringList.size();
        for(int i=0; i<numberStringListCount; i++) {
            if(numberStringList.get(i).isEmpty()) continue;
            if(numberStringList.get(i).startsWith("+") || numberStringList.get(i).startsWith("-")
                    || numberStringList.get(i).startsWith("x") || numberStringList.get(i).startsWith("/")) {
                divisionList.add(numberStringList.get(i));
            }

            int index = divisionList.size()-1;
            if(!numberStringList.get(i).startsWith("+") && !numberStringList.get(i).startsWith("-")
                    && !numberStringList.get(i).startsWith("x") && !numberStringList.get(i).startsWith("/")) {
                if(!divisionList.get(index).startsWith("/") && !divisionList.get(index).startsWith("x")) {
                    divisionList.set(index, divisionList.get(index) + numberStringList.get(i));
                }
                if(divisionList.get(index).startsWith("/") || divisionList.get(index).startsWith("x")) {
                    divisionList.add(numberStringList.get(i));
                }
            }
        }


        System.out.println(divisionList + " divisionList");
        int i=0;

        while(divisionList.contains("/")) {
            if(divisionList.get(i).startsWith("/")) {
                Double operandA = Double.parseDouble(divisionList.get(i-1));
                Double operandB = Double.parseDouble(divisionList.get(i+1));
                Double result = operandA / operandB;
                if(result > 0) {
                    divisionList.set(i-1, "+" + String.valueOf(result));
                } else {
                    divisionList.set(i-1, String.valueOf(result));
                }
                divisionList.remove(i+1);
                divisionList.remove(i);
                i--;
            } else {
                i++;
            }
        }

        currentContent = TextUtils.join("", divisionList);
        return currentContent;
    }

    private double add(String numberStrings) {
        if(!numberStrings.startsWith("-") && !numberStrings.startsWith("+")) numberStrings = "+" + numberStrings;

        ArrayList<String> operandList = new ArrayList<>(Arrays.asList(numberStrings.split("[+-]")));
        ArrayList<String> operatorList = new ArrayList<>(Arrays.asList(numberStrings.split("[\\d]")));

        while (operandList.contains("")) {
            operandList.remove(operandList.indexOf(""));
        }
        while (operatorList.contains("")) {
            operatorList.remove(operatorList.indexOf(""));
        }
        while (operatorList.contains(".")) {
            operatorList.remove(operatorList.indexOf("."));
        }

        int operandListCount = operandList.size();
        Double finalResult = 0.0;
        for(int i=0; i<operandListCount; i++) {
            finalResult = finalResult + Double.parseDouble(operatorList.get(i) + operandList.get(i));
        }
        return finalResult;
    }


    private void convertToCelsius() {
        if(!result.getText().toString().isEmpty()) {
            String stringValue = result.getText().toString();
            double doubleValue = Double.parseDouble(stringValue) - 273;
            String stringResult = String.valueOf(doubleValue);
            result.setText(stringResult);
            return;
        }
        calculate();
        double doubleValue = Double.parseDouble(currentContent) - 273;
        String stringValue = String.valueOf(doubleValue);
        result.setText(stringValue);
    }

    private void convertToFahrenheit() {
        if(result.getText() != "") {
            String stringValue = result.getText().toString();
            double celsiusValue = Double.parseDouble(stringValue) - 273;
            String stringResult = String.valueOf(celsiusValue * 9/5 + 32);
            result.setText(stringResult);
            return;
        }
        calculate();
        double celsiusValue = Double.parseDouble(currentContent) - 273;
        String stringValue = String.valueOf(celsiusValue * 9/5 + 32);
        result.setText(stringValue);
    }

}
