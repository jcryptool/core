// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * Converter Class for casting dataObject properties to/from Hashtable
 * 
 * @author Thomas Wiese
 * @version 0.1
 */
public class DataObjectConverter {

    final static String TRANSFORM_UPPERCASE = "uppercase"; //$NON-NLS-1$
    final static String TRANSFORM_LOWERCASE = "lowercase"; //$NON-NLS-1$
    final static String TRANSFORM_SEPARATOR = " | "; //$NON-NLS-1$

    /**
     * Converting dataObject to Hashtable
     * 
     * @param dataObject
     * @return Hashtable
     */
    public static Hashtable<String, String> propertiesToHashtable(Object dataobject) {
        Hashtable<String, String> props = new Hashtable<String, String>();

        Field[] fields = dataobject.getClass().getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            String value = ""; //$NON-NLS-1$
            Method[] methods = dataobject.getClass().getMethods();
            if (!key.equals("output") //$NON-NLS-1$
                    && !key.equals("inputStream") //$NON-NLS-1$
                    && !key.equals("outputStream") //$NON-NLS-1$
                    && !key.equals("opmode") //$NON-NLS-1$
                    && !key.equals("plain") //$NON-NLS-1$
                    && !key.equals("cypherAlphabet")) { //$NON-NLS-1$
                for (Method method : methods) {
                    if (method.getName().toUpperCase().equals("GET" + field.getName().toUpperCase()) //$NON-NLS-1$
                            || method.getName().toUpperCase().equals("IS" + field.getName().toUpperCase())) { //$NON-NLS-1$
                        try {
                            Object o = method.invoke(dataobject);
                            if (o != null) {
                                if (o.getClass().isArray()) {
                                    if (o instanceof char[]) {
                                        value = new String((char[]) o);
                                    } else if (o instanceof byte[]) {
                                        byte[] bytes = (byte[]) o;
                                        for (byte b : bytes) {
                                            value += String.valueOf(b) + ","; //$NON-NLS-1$
                                        }
                                    } else if (o instanceof boolean[]) {
                                        boolean[] bools = (boolean[]) o;
                                        for (boolean b : bools) {
                                            value += Boolean.toString(b) + ","; //$NON-NLS-1$
                                        }
                                    } else
                                        LogUtil.logWarning(key
                                                + " could not be serialized because of type " + o.getClass().getName()); //$NON-NLS-1$
                                } else if (o instanceof Character) {
                                    byte b = (byte) (((Character) o).charValue());
                                    value = String.valueOf(b);
                                } else if ("java.lang.Boolean".equals(o.getClass().getName())) { //$NON-NLS-1$
                                    boolean b = ((Boolean) o).booleanValue();
                                    value = Boolean.toString(b);
                                } else if (o.getClass().isPrimitive()) {
                                    value = String.valueOf(o);
                                } else if (o instanceof String) {
                                    value = (String) o;
                                } else if (o instanceof TransformData) {
                                    TransformData td = (TransformData) o;
                                    value = td.toString();

                                } else if (o instanceof Serializable) {
                                    ByteArrayOutputStream s = new ByteArrayOutputStream();
                                    ObjectOutputStream os = new ObjectOutputStream(s);
                                    os.writeObject(o);
                                    os.flush();
                                    value = new String(s.toByteArray(), "8859_1"); //$NON-NLS-1$
                                    os.close();
                                } else
                                    LogUtil.logWarning(key
                                            + " could not be serialized because of type " + o.getClass().getName()); //$NON-NLS-1$
                            } else
                                value = "null"; //$NON-NLS-1$
                        } catch (Exception ex) {
                            LogUtil.logError(ex);
                        }
                    }
                }
                props.put(key, value);
            }
        }

        return props;
    }

    /**
     * Converting Hashtable to dataObject
     * 
     * @param Hashtable
     * @return dataObject
     */
    public static IDataObject hashtableToProperties(Hashtable<String, String> props, String dataObjectType) {
        IDataObject dataobject = null;
        if (dataObjectType == null || "".equals(dataObjectType)) { //$NON-NLS-1$
            return null;
        }
        try {
            dataobject = (IDataObject) Class.forName(dataObjectType).newInstance();
            Enumeration<String> iter = props.keys();
            while (iter.hasMoreElements()) {
                String key = iter.nextElement();
                String value = (String) props.get(key);
                Method setter = null;
                try {
                    Field field = dataobject.getClass().getDeclaredField(key);
                    String type = field.getType().getName();

                    Method[] methods = dataobject.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().toUpperCase().equals("SET" + key.toUpperCase())) { //$NON-NLS-1$
                            if (method.getParameterTypes().length == 1)
                                setter = method;
                            break;
                        }
                    }
                    if (setter != null) {
                        if (field.getType().isArray()) {
                            if ("[C".equals(type)) { //$NON-NLS-1$
                                char[] _x = value.toCharArray();
                                setter.invoke(dataobject, _x);
                            } else if ("[B".equals(type)) { //$NON-NLS-1$
                                StringTokenizer t = new StringTokenizer(value, ","); //$NON-NLS-1$
                                byte[] _x = new byte[t.countTokens()];
                                int i = 0;
                                while (t.hasMoreElements()) {
                                    _x[i++] = (Byte.parseByte((String) t.nextElement()));
                                }
                                setter.invoke(dataobject, _x);
                            } else if ("[Z".equals(type)) { //$NON-NLS-1$
                                StringTokenizer t = new StringTokenizer(value, ","); //$NON-NLS-1$
                                boolean[] _x = new boolean[t.countTokens()];
                                int i = 0;
                                while (t.hasMoreElements()) {
                                    _x[i++] = (Boolean.parseBoolean((String) t.nextElement()));
                                }
                                setter.invoke(dataobject, _x);
                            }
                        } else if ("char".equals(type)) { //$NON-NLS-1$
                            char _x = (char) Byte.parseByte(value);
                            setter.invoke(dataobject, _x);
                        } else if ("boolean".equals(type)) { //$NON-NLS-1$
                            boolean _x = Boolean.parseBoolean(value);
                            setter.invoke(dataobject, _x);
                        } else if (field.getType().isPrimitive()) {
                            setter.invoke(dataobject, value);
                        } else if ("String".equals(type)) { //$NON-NLS-1$
                            setter.invoke(dataobject, value);
                        } else if (type.endsWith("TransformData")) { //$NON-NLS-1$
                            TransformData td = new TransformData();
                            td = TransformData.fromString(value);
                            setter.invoke(dataobject, td);
                        } else {
                            Class<?>[] interfaces = field.getType().getInterfaces();
                            for (Class<?> i : interfaces) {
                                if ("Serializable".equals(i.getName())) { //$NON-NLS-1$
                                    ByteArrayInputStream s = new ByteArrayInputStream(value.getBytes());
                                    ObjectInputStream os = new ObjectInputStream(s);
                                    Object _x = os.readObject();
                                    setter.invoke(dataobject, _x);
                                }
                            }
                        }
                    } else
                        LogUtil.logWarning("No setter method found for property " + key); //$NON-NLS-1$
                } catch (java.lang.IllegalArgumentException e) {
                    String m = dataobject.getClass().getName() + "." + setter.getName() + "("; //$NON-NLS-1$ //$NON-NLS-2$
                    for (Class<?> c : setter.getParameterTypes()) {
                        m += c.getName() + ","; //$NON-NLS-1$
                    }
                    m += ")"; //$NON-NLS-1$
                    LogUtil.logError(OperationsPlugin.PLUGIN_ID, m, e, false);
                } catch (NoSuchFieldException nsfe) {
                    LogUtil.logWarning("Field " + key + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        } catch (ClassNotFoundException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Class not found:" + dataObjectType); //$NON-NLS-1$
        } catch (Exception e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
        }
        return dataobject;
    }

}
