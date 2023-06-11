package hello;

import com.jme3.anim.AnimComposer;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class BonusControl extends AbstractControl implements PhysicsCollisionListener {

    private boolean collided=false;
    private final Node bonuses;
    private AnimComposer animComposer=null;
    
    public BonusControl(Node rootNode) {
        bonuses=(Node)rootNode.getChild("Bonus");
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (collided){
            // increase Dino size
            System.out.println("Collided");
            collided=false;
            Spatial dino=getSpatial();
            SizeControl sc=dino.getControl(SizeControl.class);
            sc.setIncrease(true);
            sc.increaseSize();
            sc.setIncrease(false);
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    private AnimComposer getAnimComposer() {
        if (animComposer==null) {
            animComposer=findAnimComposer(getSpatial());
        }
        return animComposer;
    }

    private AnimComposer findAnimComposer(Spatial s) {
        AnimComposer composer = s.getControl(AnimComposer.class);
        if (composer != null) {
            return composer;
        }
        if (s instanceof Node) {
            Node node = (Node) s;
            for (Spatial child : node.getChildren()) {
                composer = findAnimComposer(child);
                if (composer != null) {
                    return composer;
                }
            }
        }
        return null;
    }
    
    private void removeBonus(Spatial other) {
        Node parent=other.getParent();
        RigidBodyControl rbc=other.getControl(RigidBodyControl.class);
        if (rbc!=null)
            rbc.getPhysicsSpace().remove(rbc);
        if (parent==bonuses)
            parent.detachChild(other);
        else if (parent!=null)
            removeBonus(parent);
    }
    
    private void setAction(String action, float speed) {
        AnimComposer a=getAnimComposer();
        if (a!=null) {
            a.setCurrentAction(action);
            a.setGlobalSpeed(speed);
        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        Spatial dino=getSpatial();
        Spatial other;
        if (event.getNodeA()==dino)
            other=event.getNodeB();
        else if (event.getNodeB()==dino)
            other=event.getNodeA();
        else
            return;
        
        if (other.getParent()!=bonuses)
            return;
        setAction("bite",1.0f);
        System.out.println("Bite bonus");
        removeBonus(other);
        collided=true;
    }
    
}
