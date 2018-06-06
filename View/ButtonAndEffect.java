package View;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Tomash on 22-Jun-17.
 */
public class ButtonAndEffect {

    private SimpleStringProperty button = new SimpleStringProperty("");
    private SimpleStringProperty effect = new SimpleStringProperty("");

    public ButtonAndEffect(String button, String effect) {
        setButton(button);
        setEffect(effect);
    }

    public ButtonAndEffect(){
        this("","");
    }

    public String getButton() {
        return button.get();
    }

    public void setButton(String button) {
        this.button.set(button);
    }

    public String getEffect() {
        return effect.get();
    }

    public void setEffect(String effect) {
    this.effect.set(effect);
    }
}
