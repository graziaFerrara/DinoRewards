package hello;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

public class DamageControl extends AbstractControl implements PhysicsCollisionListener
        
                            {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    private static final float DURATION=3.0f,
                               ACCELERATION=9.8f,
                               INITIAL_SPEED=0.5f*ACCELERATION*DURATION,
                               OMEGA=2.0f*FastMath.PI*5.0f;
    private boolean collided=false;
    private boolean dead=false;
    private final Node enemies;
    private float time;
    
    public DamageControl(Node rootNode) {
        enemies=(Node)rootNode.getChild("Enemies");
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (dead)
            updateDead(tpf);
        else
            updateAlive(tpf);
    }
        
        
    private void updateAlive(float tpf) {
        if (collided) {
            collided=false;
            dead=true;
            time=0.0f;
            Spatial dino=getSpatial();
            MotionControl mc=dino.getControl(MotionControl.class);
            mc.setEnabled(false);
            RigidBodyControl rbc=dino.getControl(RigidBodyControl.class);
            rbc.setLinearVelocity(new Vector3f(0.0f, INITIAL_SPEED, 0.0f));
            rbc.setAngularVelocity(new Vector3f(0.0f, OMEGA, 0.0f));
        }
    }
    
    private void updateDead(float tpf) {
        time+=tpf;
        Spatial dino=getSpatial();
        if (time>=DURATION) {
            dead=false;
            
            MotionControl mc=dino.getControl(MotionControl.class);
            mc.setEnabled(true);
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    private void removeEnemy(Spatial other) {
        Node parent=other.getParent();
        RigidBodyControl rbc=other.getControl(RigidBodyControl.class);
        if (rbc!=null)
            rbc.getPhysicsSpace().remove(rbc);
        if (parent==enemies)
            parent.detachChild(other);
        else if (parent!=null)
            removeEnemy(parent);
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
        
        if (other.getParent()!=enemies)
            return;
        
        removeEnemy(other);
        collided=true;
    }
    
}
