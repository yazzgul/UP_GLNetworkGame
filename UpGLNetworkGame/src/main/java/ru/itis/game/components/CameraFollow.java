package ru.itis.game.components;

import org.joml.Vector4f;
import ru.itis.gengine.gamelogic.Component;
import ru.itis.gengine.gamelogic.components.Transform;

public class CameraFollow extends Component {
    Transform target;
    Transform transform;

    public CameraFollow(int id, Transform target) {
        super(id, false);
        this.target = target;
    }

    @Override
    public void initialize() {
        transform = getEntity().getTransform();
    }

    @Override
    public void update(float deltaTime) {
        Vector4f targetPosition = target.getPosition();
        transform.setPosition(0.f, targetPosition.y, 10.f);
    }
}
