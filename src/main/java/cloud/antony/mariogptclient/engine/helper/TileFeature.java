package cloud.antony.mariogptclient.engine.helper;

import java.util.ArrayList;

public enum TileFeature
{
    BLOCK_UPPER, 
    BLOCK_ALL, 
    BLOCK_LOWER, 
    SPECIAL, 
    LIFE, 
    BUMPABLE, 
    BREAKABLE, 
    PICKABLE, 
    ANIMATED, 
    SPAWNER;
    
    public static ArrayList<TileFeature> getTileType(final int n) {
        final ArrayList<TileFeature> list = new ArrayList<TileFeature>();
        switch (n) {
            case 1:
            case 2:
            case 4:
            case 5:
            case 14:
            case 18:
            case 19:
            case 20:
            case 21:
            case 52:
            case 53: {
                list.add(TileFeature.BLOCK_ALL);
                break;
            }
            case 43:
            case 44:
            case 45:
            case 46: {
                list.add(TileFeature.BLOCK_LOWER);
                break;
            }
            case 48: {
                list.add(TileFeature.BLOCK_UPPER);
                list.add(TileFeature.LIFE);
                list.add(TileFeature.BUMPABLE);
                break;
            }
            case 49: {
                list.add(TileFeature.BUMPABLE);
                list.add(TileFeature.BLOCK_UPPER);
                break;
            }
            case 3: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.SPAWNER);
                break;
            }
            case 8: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.SPECIAL);
                list.add(TileFeature.BUMPABLE);
                list.add(TileFeature.ANIMATED);
                break;
            }
            case 11: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.BUMPABLE);
                list.add(TileFeature.ANIMATED);
                break;
            }
            case 6: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.BREAKABLE);
                break;
            }
            case 7: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.BUMPABLE);
                break;
            }
            case 15: {
                list.add(TileFeature.PICKABLE);
                list.add(TileFeature.ANIMATED);
                break;
            }
            case 50: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.SPECIAL);
                list.add(TileFeature.BUMPABLE);
                break;
            }
            case 51: {
                list.add(TileFeature.BLOCK_ALL);
                list.add(TileFeature.LIFE);
                list.add(TileFeature.BUMPABLE);
                break;
            }
        }
        return list;
    }
}
