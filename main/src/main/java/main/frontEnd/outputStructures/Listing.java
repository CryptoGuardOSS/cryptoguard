package main.frontEnd.outputStructures;

import main.frontEnd.OutputStructure;

/**
 * The enum containing all of the different messaging types available for the user.
 */
public enum Listing
{
	//region Different Values
	LegacyOutput("LegacyOutput", "L");
	//endregion

	//region Attributes
	private String type;
	private String flag;
	//endregion

	//region Constructor

	/**
	 * The inherint constructor of all the enum value types listed here
	 *
	 * @param Type - the string value of the type of
	 * @param Flag - the flag used to identify the specific messaging type
	 */
	Listing(String Type, String Flag)
	{
		this.type = Type;
		this.flag = Flag;
	}
	//endregion

	//region Overridden Methods
	@Override
	public String toString()
	{
		return "{ \"type\": \"" + this.type + "\", \"flag\": \"" + this.flag + "\"}";
	}
	//endregion

	/**
	 * A method to dynamically retrieve the type of messaging structure asked for by the flag type.
	 * NOTE: if there is any kind of issue instantiating the class name, it will default to the Legacy Output
	 *
	 * @param flag - the type of Messaging Structure asked for
	 * @return outputStructure - the type of messaging structure to be used to return information
	 */
	public static OutputStructure getTypeOfMessaging(String flag)
	{
		try
		{
			String className;
			switch (flag)
			{
				default:
					className = LegacyOutput.type;
					break;
			}

			//Return a dynamically loaded instantiation of the class
			return (OutputStructure) Class.forName("main.frontEnd.outputStructures." + className).newInstance();
		}
		//In Case of any error, default to the Legacy Output
		catch (Exception e)
		{
			return new main.frontEnd.outputStructures.LegacyOutput();
		}
	}
}
