package net.xy360.commonutils.realm;

import android.content.Context;

import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.CopyCart;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.PrintingCart;

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

    public static void removeAllData() {
        RealmHelper.realm.beginTransaction();
        RealmHelper.realm.clear(Cart.class);
        RealmHelper.realm.clear(CopyCart.class);
        RealmHelper.realm.clear(Copy.class);
        RealmHelper.realm.clear(PrintingCart.class);
        RealmHelper.realm.clear(File.class);
        RealmHelper.realm.commitTransaction();
    }
}
