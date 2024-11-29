package io.github.desynq.commandsurvival.data;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public class OrientedBoundingBox {
    public Vec3 center;
    public Vec3 extent;

    public Vec3 axisX;
    public Vec3 axisY;
    public Vec3 axisZ;

    public Vec3 scaledAxisX;
    public Vec3 scaledAxisY;
    public Vec3 scaledAxisZ;

    public Matrix3f rotation = new Matrix3f();
    public Vec3 vertex1;
    public Vec3 vertex2;
    public Vec3 vertex3;
    public Vec3 vertex4;
    public Vec3 vertex5;
    public Vec3 vertex6;
    public Vec3 vertex7;
    public Vec3 vertex8;
    public Vec3[] vertices;

    public OrientedBoundingBox(Vec3 center, double width, double height, double depth, float yaw, float pitch) {
        this.center = center;
        this.extent = new Vec3(width/2.0, height/2.0, depth/2.0);
        this.axisZ = Vec3.directionFromRotation(yaw, pitch).normalize();
        this.axisY = Vec3.directionFromRotation(yaw + 90, pitch).reverse().normalize();
        this.axisX = axisZ.cross(axisY);
    }

    public OrientedBoundingBox(Vec3 center, Vec3 size, float yaw, float pitch) {
        this(center,size.x, size.y, size.z, yaw, pitch);
    }
}
