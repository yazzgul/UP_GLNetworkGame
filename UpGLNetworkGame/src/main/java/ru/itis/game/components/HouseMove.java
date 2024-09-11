package ru.itis.game.components;

import org.joml.Vector4f;
import ru.itis.game.network.ObjectPosition;
import ru.itis.gengine.base.Direction;
import ru.itis.gengine.events.Events;
import ru.itis.gengine.gamelogic.Component;
import ru.itis.gengine.gamelogic.Entity;
import ru.itis.gengine.gamelogic.Physics;
import ru.itis.gengine.gamelogic.components.Transform;
import ru.itis.gengine.network.model.NetworkComponentState;

public class HouseMove extends Component {
    private Events events;
    private Transform transform;
    private Physics physics;
    private Direction direction;
    private final float speed = 3.f;

    public HouseMove(int id) {
        super(id, true);
    }

    @Override
    public void initialize() {
        events = getEntity().getEvents();
        physics = getEntity().getPhysics();
        transform = getEntity().getTransform();
        direction = Direction.Right;
    }

    @Override
    public void update(float deltaTime) {
    }
    @Override
    public NetworkComponentState getState() {
        Vector4f coordinates = transform.getPosition();
        return new ObjectPosition(coordinates.x, coordinates.y);
    }

    @Override
    public void setState(NetworkComponentState state) {
        ObjectPosition position = (ObjectPosition) state;
        transform.setPosition(position.x(), position.y(), 0.f);
    }
}