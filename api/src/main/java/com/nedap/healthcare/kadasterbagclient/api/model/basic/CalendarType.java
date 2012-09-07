package com.nedap.healthcare.kadasterbagclient.api.model.basic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.MutableType;
import org.hibernate.type.VersionType;
import org.hibernate.util.CalendarComparator;

/**
 * {@link Calendar} {@link MutableType} implementation for allowing using calendar properties in entity objects.
 * 
 * @author dvesin
 */
public class CalendarType extends MutableType implements VersionType {

    public static final String NAME = "Calendar";

    private static final long serialVersionUID = -7604640802996273590L;

    @Override
    public Object get(final ResultSet rs, final String name) throws SQLException {

        final Long ts = rs.getLong(name);
        if (ts != null) {
            final Calendar cal = Calendar.getInstance();
            if (Environment.jvmHasTimestampBug()) {
                cal.setTimeInMillis(ts);
            } else {
                cal.setTimeInMillis(ts);
            }
            return cal;
        } else {
            return null;
        }

    }

    @Override
    public final void set(final PreparedStatement st, final Object value, final int index) throws SQLException {
        final Calendar cal = (Calendar) value;
        // TODO - st.setTimestamp( index, new Timestamp( cal.getTimeInMillis() ), cal ); //JDK 1.5 only
        st.setLong(index, cal.getTime().getTime());
    }

    @Override
    public final int sqlType() {
        return Types.BIGINT;
    }

    @Override
    public final String toString(final Object value) {
        return Hibernate.TIMESTAMP.toString(((Calendar) value).getTime());
    }

    @Override
    public final Object fromStringValue(final String xml) {
        final Calendar result = Calendar.getInstance();
        result.setTime((Date) Hibernate.TIMESTAMP.fromStringValue(xml));
        return result;
    }

    @Override
    public final Object deepCopyNotNull(final Object value) {
        return ((Calendar) value).clone();
    }

    @Override
    public final Class<?> getReturnedClass() {
        return Calendar.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final int compare(final Object x, final Object y, final EntityMode entityMode) {
        return CalendarComparator.INSTANCE.compare(x, y);
    }

    @Override
    public final boolean isEqual(final Object x, final Object y) {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }

        final Calendar calendar1 = (Calendar) x;
        final Calendar calendar2 = (Calendar) y;

        return calendar1.get(Calendar.MILLISECOND) == calendar2.get(Calendar.MILLISECOND)
                && calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND)
                && calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)
                && calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    @Override
    public final int getHashCode(final Object x, final EntityMode entityMode) {
        final Calendar calendar = (Calendar) x;
        int hashCode = 1;
        final int coefficient = 31;
        hashCode = coefficient * hashCode + calendar.get(Calendar.MILLISECOND);
        hashCode = coefficient * hashCode + calendar.get(Calendar.SECOND);
        hashCode = coefficient * hashCode + calendar.get(Calendar.MINUTE);
        hashCode = coefficient * hashCode + calendar.get(Calendar.HOUR_OF_DAY);
        hashCode = coefficient * hashCode + calendar.get(Calendar.DAY_OF_MONTH);
        hashCode = coefficient * hashCode + calendar.get(Calendar.MONTH);
        hashCode = coefficient * hashCode + calendar.get(Calendar.YEAR);
        return hashCode;
    }

    @Override
    public final String getName() {
        return "calendar";
    }

    @Override
    public final Object next(final Object current, final SessionImplementor session) {
        return seed(session);
    }

    @Override
    public final Object seed(final SessionImplementor session) {
        return Calendar.getInstance();
    }

    @Override
    public final Comparator<?> getComparator() {
        return CalendarComparator.INSTANCE;
    }

}
