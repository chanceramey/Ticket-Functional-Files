package command;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by tjense25 on 1/27/18.
 */

public class Command {
    private static Gson gson = new Gson();
    
    private String className;
    private String methodName;
    private String[] parameterTypesNames;
    private Object[] parameters;
    private String[] parametersAsJsonStrings;
    private Class<?>[] parameterTypes;
    
    public Command(String className, String methodName, String[] parameterTypesNames, Object[] parameters) {
	    this.className = className;
        this.methodName = methodName;
        this.parameterTypesNames = parameterTypesNames;
        this.parametersAsJsonStrings = new String[parameterTypesNames.length];
        for (int i = 0; i < parameters.length; i++) {
            parametersAsJsonStrings[i] = gson.toJson(parameters[i]);
        }
    }
    
    public Command(InputStreamReader inputStreamReader) {
        Command tempCommand = gson.fromJson(inputStreamReader, Command.class);
        
	    this.className = tempCommand.getClassName();
        this.methodName = tempCommand.getMethodName();
        this.parameterTypesNames = tempCommand.getParameterTypesNames();
        this.parametersAsJsonStrings = tempCommand.getParametersAsJsonStrings();
    }

    private void prepareCommand() {
        createParameterTypes();
        this.parameters = new Object[parametersAsJsonStrings.length];
        for(int i = 0; i < parametersAsJsonStrings.length; i++) {
            parameters[i] = gson.fromJson(parametersAsJsonStrings[i], parameterTypes[i]);
        }
    }


    public Object execute() {
        prepareCommand();
        Object result = null;

        try {
            Class<?> klass = Class.forName(className);
	        Object target = klass.newInstance();
	        Method[] methods = klass.getMethods();
            Method method = klass.getMethod(methodName, parameterTypes);
            result = method.invoke(target, parameters);
	    } catch(ClassNotFoundException e) {
		    System.err.println("ERROR: Could not find the class " + className);
        } catch(InstantiationException e) {
            System.err.println("ERROR: Could not instantiate the class " + className);
        } catch (NoSuchMethodException e) {
            System.err.println("ERROR: Could not find the method " + methodName);
            System.out.println("className:" + className);
            System.out.println("methodName:" + methodName);
            int counter = 0;
            for (String str: parametersAsJsonStrings) {
                System.out.println("Parameter "+ counter + ": " + str);
            }
            for (String str: parameterTypesNames) {
                System.out.println("ParameterType "+ counter + ": " + str);
            }
            for (Object o: parameters) {
                System.out.println("Actual Parameter Type "+ counter + ": " + o.getClass());
            }
        } catch (IllegalAccessException e) {
            System.err.println("ERROR: Illegal access while trying to execute the method " + methodName);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Illegal argument while trying to find the method " + methodName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("ERROR: Illegal Invocation on the target class when calling " + methodName + " on " + className);
            e.printStackTrace();
        } catch (SecurityException e) {
            System.err.println("ERROR: Security error when trying to access the method " + methodName);
        }

        return result;
    }

    private final void createParameterTypes() {
        parameterTypes = new Class<?>[parameterTypesNames.length];
        for(int i = 0; i < parameterTypesNames.length; i++) {
            try {
                parameterTypes[i] = getClassFor(parameterTypesNames[i]);
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR: In Command.execute could not create a aparmeter type from " +
                        "the parameter type name " + parameterTypesNames[i]);
                e.printStackTrace();
            }
        }
    }

    private Class<?> getClassFor(String className) throws ClassNotFoundException {
        Class<?> result = null;
        switch(className) {
            case "boolean" :
                result = boolean.class; break;
            case "byte"    :
                result = byte.class;    break;
            case "char"    :
                result = char.class;    break;
            case "double"  :
                result = double.class;  break;
            case "float"   :
                result = float.class;   break;
            case "int"     :
                result = int.class;     break;
            case "long"    :
                result = long.class;    break;
            case "short"   :
                result = short.class;   break;
            default:
                result = Class.forName(className);
        }
        return result;
    }

    public String getClassName() {
	return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypesNames() {
        return parameterTypesNames;
    }

    public String[] getParametersAsJsonStrings() {
        return parametersAsJsonStrings;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public static Command createCommand(String targetClass, String methodName, Object... params) {

        String[] paramTypes = new String[params.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = params[i].getClass().getName();

        }
        return new Command(targetClass, methodName, paramTypes, params);
    }
}
