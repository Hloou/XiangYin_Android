package net.xy360.commonutils.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jolin on 2016/3/6.
 */
public class RealmHelper {
    public static RealmConfiguration getConfig(Context context) {
        return new RealmConfiguration.Builder(context)
                .name("myrealm.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(3)
                .build();
    };

    public static Realm getInstance(Context context) {
        return Realm.getInstance(getConfig(context));
    }

    public static Realm realm;
}
