package com.br.confeitarialegal.views;

import com.br.confeitarialegal.App;
import com.br.confeitarialegal.entities.User;
import com.br.confeitarialegal.controllers.UserController;
import com.br.confeitarialegal.repositories.RepositoryMethod;

import com.br.confeitarialegal.views.enums.Screens;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Daniel Vitor
 */
public class RegistrationViewController {
  
  @FXML
  AnchorPane anchorPane;
  
  @FXML
  FontAwesomeIconView togglePasswordVisible;
  
  @FXML
  FontAwesomeIconView toggleConfirmPasswordVisible;
  
  @FXML
  TextField textFieldEmail;
  
  @FXML
  PasswordField passwordField;
  
  @FXML
  PasswordField confirmPasswordField;
  
  @FXML
  Tooltip passwordTooltip;
  
  @FXML
  Tooltip confirmPasswordTooltip;
  
  private boolean showPassword;
  
  Stage stage;
  
  public RegistrationViewController() {
    this.showPassword = false;
  }
  
  private void showPassword() {
    if (this.stage == null) {
      this.stage = (Stage) anchorPane.getScene().getWindow();
    }

    Point2D p = passwordField.localToScene(passwordField.getBoundsInLocal().getMaxX(), passwordField.getBoundsInLocal().getMinY());
    passwordTooltip.setText(passwordField.getText());
    passwordTooltip.show(passwordField,
            p.getX() + stage.getScene().getX() + stage.getX(),
            p.getY() + stage.getScene().getY() + stage.getY()); 

    Point2D p2 = confirmPasswordField.localToScene(confirmPasswordField.getBoundsInLocal().getMaxX(), confirmPasswordField.getBoundsInLocal().getMinY());
    confirmPasswordTooltip.setText(confirmPasswordField.getText());
    confirmPasswordTooltip.show(confirmPasswordField,
            p2.getX() + stage.getScene().getX() + stage.getX(),
            p2.getY() + stage.getScene().getY() + stage.getY());
  }

   private void hidePassword() {
      passwordTooltip.setText("");
      passwordTooltip.hide();
      
      confirmPasswordTooltip.setText("");
      confirmPasswordTooltip.hide();
   }
  
  public void onMouseClickTogglePassword() {
    passwordTooltip.setText(passwordField.getText());
    confirmPasswordTooltip.setText(confirmPasswordField.getText());
    
    if (showPassword) {
      togglePasswordVisible.setIcon(FontAwesomeIcon.EYE_SLASH);
      toggleConfirmPasswordVisible.setIcon(FontAwesomeIcon.EYE_SLASH);
      
      hidePassword();
    } else {
      togglePasswordVisible.setIcon(FontAwesomeIcon.EYE);
      toggleConfirmPasswordVisible.setIcon(FontAwesomeIcon.EYE);
      
      showPassword();
    }

    showPassword = !showPassword;
  }
  
  public void onMouseClickRegister() {
    Alert alert;

    String passwordText = passwordField.getText();
    String confirmPasswordText = confirmPasswordField.getText();
    String email = textFieldEmail.getText();
    
    if (email.isEmpty()) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("E-mail vazio");
      alert.setContentText("Preencha o campo vazio");

      alert.show();
      return;
    }
    
    if (!email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("E-mail inválido");
      alert.setContentText("Digite um e-mail válido");

      alert.show();
      return;
    }

    if (passwordText.isEmpty() && confirmPasswordText.isEmpty()) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Senhas vazias");
      alert.setContentText("Preencha os campos e tente novamente");

      alert.show();
    } else if (passwordText.equals(confirmPasswordText)) {
      UserController userController = new UserController(RepositoryMethod.HIBERNATE);

      User user = userController.create(email, passwordText);

      if (user == null) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro!");
        alert.setContentText("Erro!");
        alert.show();
        return;
      }
      
      alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Registrado");
      alert.setContentText("Registrado");
      alert.show();
      
      System.out.println(user);

      try {
        App.setRoot(Screens.LOGIN.getRoute());
      } catch (IOException ex) {
        Logger.getLogger(RegistrationViewController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Senhas diferentes");
      alert.setContentText("As senhas não podem ser diferentes");

      alert.show();
    }

  }
}
