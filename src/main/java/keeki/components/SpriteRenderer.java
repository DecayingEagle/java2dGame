package keeki.components;

import keeki.jade.Component;

public class SpriteRenderer extends Component{
    private boolean firstTime = false;

    @Override
    public void start() {
        System.out.println("SpriteRenderer.start()");
    }

    @Override
    public void update(float dt) {
        if (!firstTime) {
            System.out.println("SpriteRenderer.update()");
            firstTime = true;
        } 
    }
    
}
