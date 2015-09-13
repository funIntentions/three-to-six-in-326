package com.mygdx.projectMeta.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Dan on 9/12/2015.
 */
public class Utils {

    public static double randomClamped()
    {
        return (MathUtils.random() + MathUtils.random() - 1);
    }

    public static Vector3 pointToWorldSpace(Vector3 point, Vector3 heading, Vector3 side, Vector3 pos)
    {
        //make a copy of the point
        Vector3 transPoint = point;
        float[] matValues = new float[] {heading.x,0,heading.z,
                                            0,1,0,
                                            side.x,0,side.z};

        //create a transformation matrix
        Matrix3 matTransform = new Matrix3(matValues);

        //and translate
        matTransform.translate(pos.x, pos.z);

        //Console.WriteLine("Trans " + matTransform);

        //now transform the vertex
        transPoint.traMul(matTransform);

        //Console.WriteLine(TransPoint);

        return transPoint;
    }
}
