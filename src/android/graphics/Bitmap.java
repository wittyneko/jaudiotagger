package android.graphics;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.OutputStream;

public class Bitmap {

    public static Bitmap createBitmap(@NotNull Bitmap source, int x, int y, int width, int height,
                                      @Nullable Matrix m, boolean filter) {
        return null;
    }

    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        return false;
    }

    public int getWidth() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }


    /**
     * Specifies the known formats a bitmap can be compressed into
     */
    public enum CompressFormat {
        JPEG(0),
        PNG(1),
        WEBP(2);

        CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }

        final int nativeInt;
    }
}
