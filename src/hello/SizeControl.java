package hello;

import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class SizeControl extends AbstractControl implements ActionListener{

    private boolean increaseSize = true;
    private boolean restored = true;

    @Override
    protected void controlUpdate(float tpf) {
        
    }

    public SizeControl() {
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        SizeControl control = new SizeControl();
        //TODO: copy parameters to new Control
        return control;
    }

    @Override
    public void onAction(String input, boolean active, float f) {

    }
    
    public void setIncrease(boolean increase){
        this.increaseSize = increase;
        restored = false;
    }
    
    public void increaseSize() {
        if (increaseSize){
            Spatial dino=getSpatial();
            dino.setLocalScale(dino.getLocalScale().mult(1.2f));
            System.out.println("INCREASE Dino SIZE");
        }
    }
    
    public void setRestored(){
        restored = true;
    }
    
    public boolean isRestored(){
        return restored;
    }
    
}
