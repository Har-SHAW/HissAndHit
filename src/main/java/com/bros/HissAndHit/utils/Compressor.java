package com.bros.HissAndHit.utils;

import com.bros.HissAndHit.data.PlayerData;
import com.bros.HissAndHit.data.ServerData;
import com.bros.HissAndHit.proto.Data;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;

public class Compressor {
    public static byte[] serialize(Deque<int[]>[] data) throws IOException {
        Data.Array3D.Builder array3DBuilder = Data.Array3D.newBuilder();

        for (Deque<int[]> array2D : data) {
            Data.Array2D.Builder array2DBuilder = Data.Array2D.newBuilder();
            for (int[] array1D : array2D) {
                Data.Point.Builder array1DBuilder = Data.Point.newBuilder();
                array1DBuilder.setX(array1D[0]).setY(array1D[1]);
                array2DBuilder.addArray1S(array1DBuilder);
            }
            array3DBuilder.addArray2S(array2DBuilder.build());
        }

        Data.Array3D array3D = array3DBuilder.build();
        Data.Food.Builder foodBuilder = Data.Food.newBuilder();

        for (Map.Entry<Integer, Data.Point> entry : ServerData.foodMap.entrySet()) {
            foodBuilder.addPoint(entry.getValue());
        }

        Data.FinalData finalData = Data.FinalData.newBuilder()
                .setMetaData(ServerData.metaData)
                .setPosition(array3D)
                .setFood(foodBuilder.build()).build();

        return finalData.toByteArray();
    }

    public static void deserialize(byte[] data) throws InvalidProtocolBufferException {
        Data.FinalData finalData = Data.FinalData.parseFrom(data);

        PlayerData.positions = finalData.getPosition();
        PlayerData.food = finalData.getFood();
        PlayerData.metaData = finalData.getMetaData();
    }
}
