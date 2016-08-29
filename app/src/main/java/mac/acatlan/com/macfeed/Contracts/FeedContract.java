package mac.acatlan.com.macfeed.Contracts;

import android.provider.BaseColumns;

/**
 * Created by jhoan on 2/6/16.
 */
public final class FeedContract {
    public static final String DATABASE_NAME = "feed";
    public static final int DATABASE_VERSION = 2;
    public FeedContract() {

    }

    public static abstract class AvisoContract implements BaseColumns {
        public static final String TABLE_NAME = "aviso";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_DESCRIPCION = "descripcion";
        public static final String COLUMN_NAME_FECHA_CREACION = "created_at";
        public static final String COLUMN_NAME_TIPO = "categoria";
    }

    public static abstract class AdjuntoContract implements BaseColumns {
        public static final String TABLE_NAME = "adjunto";
        public static final String COLUMN_NAME_AVISO_ID = "aviso_id";
        public static final String COLUMN_NAME_TITULO = "titulo";
        public static final String COLUMN_NAME_LINK = "link";
    }
}
