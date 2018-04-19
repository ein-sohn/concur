package com.woongjin.concur.awt;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.woongjin.framework.core.encrypt.AES256Cipher;
 
public class Cipher {
    private Frame mainFrame;
    private Label headerLabel;
    private Panel controlPanel;
    private TextField inputField ;
    private TextField outputField ;
 
    public Cipher() {
        prepareGUI();
    }
 
    public static void main(String[] args) {
        Cipher awtControlDemo = new Cipher();
        awtControlDemo.showButton();
    }
 
    private void prepareGUI() {
        // Frame 에 대한 셋팅
        mainFrame = new Frame("Encoding / Decoding - woongjin(concur)");
        mainFrame.setSize(400, 200);
        mainFrame.setLayout(new GridLayout(4, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        // 상단에 있는 라벨
        headerLabel = new Label();
        headerLabel.setAlignment(Label.CENTER);
        headerLabel.setText("암/복호화 할 텍스트를 입력하시오.");
 
        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());
        
        inputField =new TextField("");
 
        outputField =new TextField("");
        outputField.setEditable(false);        

        mainFrame.add(headerLabel);
        mainFrame.add(inputField);
        mainFrame.add(outputField);
    }

    private void showButton() {
 
        Button btnEncode = new Button("Encode");
        Button btnDecode = new Button("Decode");
 
        btnEncode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		outputField.setText(AES256Cipher.AES_Encode(inputField.getText()));
				} catch (InvalidKeyException e1) {
            		outputField.setText(e1.getMessage());
				} catch (UnsupportedEncodingException e1) {
            		outputField.setText(e1.getMessage());
				} catch (NoSuchAlgorithmException e1) {
            		outputField.setText(e1.getMessage());
				} catch (NoSuchPaddingException e1) {
            		outputField.setText(e1.getMessage());
				} catch (InvalidAlgorithmParameterException e1) {
            		outputField.setText(e1.getMessage());
				} catch (IllegalBlockSizeException e1) {
            		outputField.setText(e1.getMessage());
				} catch (BadPaddingException e1) {
            		outputField.setText(e1.getMessage());
				}
            }
        });
 
        btnDecode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		outputField.setText(AES256Cipher.AES_Decode(inputField.getText()));
				} catch (InvalidKeyException e1) {
            		outputField.setText(e1.getMessage());
				} catch (UnsupportedEncodingException e1) {
            		outputField.setText(e1.getMessage());
				} catch (NoSuchAlgorithmException e1) {
            		outputField.setText(e1.getMessage());
				} catch (NoSuchPaddingException e1) {
            		outputField.setText(e1.getMessage());
				} catch (InvalidAlgorithmParameterException e1) {
            		outputField.setText(e1.getMessage());
				} catch (IllegalBlockSizeException e1) {
            		outputField.setText(e1.getMessage());
				} catch (BadPaddingException e1) {
            		outputField.setText(e1.getMessage());
				}
            }
        });
 
        controlPanel.add(btnEncode);
        controlPanel.add(btnDecode);
        
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
        
    }
}
