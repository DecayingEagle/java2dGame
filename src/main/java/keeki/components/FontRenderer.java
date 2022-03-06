package keeki.components;

import keeki.jade.Component;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if(gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("FontRenderer.start()");
        }
    }

    @Override
    public void update(float dt) {
    }
    
    
}
