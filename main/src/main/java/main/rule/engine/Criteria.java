package main.rule.engine;

/**
 * The criteria helper class. This is used for creating static rules to be enforced in various ways.
 *
 * @author RigorityJTeam
 * @since 1.0
 */
public class Criteria
{

	private String className;
	private String methodName;
	private int param;

	/**
	 * The "getter" for the class name.
	 *
	 * @return String the name of the class in the criteria
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * The "setter" for the class name.
	 *
	 * @param className the name of the class in the criteria
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * The "getter" for the method name.
	 *
	 * @return String the name of the method in the criteria
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * The "setter" for the method name.
	 *
	 * @param methodName the name of the method in the criteria
	 */
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	/**
	 * The "getter" for the parameter for the method.
	 *
	 * @return int the parameter type in the criteria
	 */
	public int getParam()
	{
		return param;
	}

	/**
	 * The "setter" for the the parameter for the method.
	 *
	 * @param param the parameter type in the criteria
	 */
	public void setParam(int param)
	{
		this.param = param;
	}
}
