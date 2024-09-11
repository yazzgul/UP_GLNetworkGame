package ru.itis.game.components;

import org.joml.Vector4f;
import ru.itis.game.network.ObjectPosition;
import ru.itis.gengine.base.Direction;
import ru.itis.gengine.events.Events;
import ru.itis.gengine.events.Key;
import ru.itis.gengine.gamelogic.Component;
import ru.itis.gengine.gamelogic.Physics;
import ru.itis.gengine.gamelogic.components.Transform;
import ru.itis.gengine.network.model.NetworkComponentState;

public class BallMove extends Component {
    private Events events;
    private Transform transform;
    private Physics physics;
    private Direction direction;
    private final float speed = 3.f;

    public BallMove(int id) {
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
        boolean playerMoved = false;

//      влево
        if (events.isKeyPressed(Key.A)) {
//            чтобы не вышел за край экрана
            if (transform.getPosition().x < -7) {
                return;
            }
            if (direction != Direction.Left) {
                transform.setRotation(0.f, (float) (Math.PI * 1.f), 0.f);
                direction = Direction.Left;
            }
            transform.translate(-speed * deltaTime, 0.f, 0.f);
            playerMoved = true;
        }

//        вправо
        if (events.isKeyPressed(Key.D)) {
//            чтобы не вышел за край экрана
            if (transform.getPosition().x > 7) {
                return;
            }
            if (direction != Direction.Right) {
                transform.setRotation(0.f, (float) (Math.PI * 1.f), 0.f);
                direction = Direction.Right;
            }
            transform.translate(speed * deltaTime, 0.f, 0.f);
            playerMoved = true;
        }

//      вверх
        if (events.isKeyPressed(Key.W)) {
            if (direction != Direction.Up) {
                direction = Direction.Up;
            }
            transform.translate(0.f, speed * deltaTime, 0.f);
            playerMoved = true;
        }

//      вниз
        if (events.isKeyPressed(Key.S)) {
            if (direction != Direction.Down) {
                direction = Direction.Down;
            }
            transform.translate(0.f, -speed * deltaTime, 0.f);
            playerMoved = true;
        }

        if (playerMoved) {
            sendCurrentState();
        }

    }

    @Override
    public NetworkComponentState getState() {
        Vector4f coordinates = transform.getPosition();
        return new ObjectPosition(coordinates.x, coordinates.y);
    }

}