package com.mygdx.projectMeta.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dan on 9/12/2015.
 */
public class Utils {

    public static double randomClamped()
    {
        return (MathUtils.random() + MathUtils.random() - 1);
    }

    public static Vector3 pointToWorldSpace(Vector2 point, Vector2 heading, Vector2 side, Vector2 pos)
    {
        //make a copy of the point
        Vector3 transPoint = new Vector3(point.x, point.y, 0);
        float[] matValues = new float[] {heading.x, heading.y, 0,
                                        side.x, side.y, 0,
                                        0, 0, 1};

        //create a transformation matrix
        Matrix3 matTransform = new Matrix3(matValues);

        //and translate
        matTransform.translate(pos.x, pos.y);

        //Console.WriteLine("Trans " + matTransform);

        //now transform the vertex
        transPoint.traMul(matTransform);

        //Console.WriteLine(TransPoint);

        return transPoint;
    }
}
