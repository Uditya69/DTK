using System.Xml.Linq;

namespace ConsoleApp;

public static class Program
{
    private static int _totalSum;

    public static void Main(string[] args)
    {
        if (args.Length < 1)
        {
            Console.WriteLine("Usage: dotnet run <input.xml>");
            return;
        }

        string xmlFile = args[0];
        try
        {
            (int num1, int num2) = ReadInitialValues(xmlFile);
            _totalSum = num1 + num2;
        }
        catch (Exception e)
        {
            Console.WriteLine($"Error reading initial values: {e.Message}");
            return;
        }

        while (true)
        {
            string? userInput = Console.ReadLine()?.Trim();

			if (userInput == null)
			{
				break;
			}

            if (userInput.Equals("exit", StringComparison.OrdinalIgnoreCase))
            {
                break;
            }

            if (userInput.Equals("print sum", StringComparison.OrdinalIgnoreCase))
            {
                Console.WriteLine(_totalSum);
            }
            else if (userInput.StartsWith("add ", StringComparison.OrdinalIgnoreCase))
            {
                try
                {
                    int number = int.Parse(userInput.Split(' ')[1]);
                    _totalSum += number;
                }
                catch (Exception)
                {
                    Console.WriteLine("Invalid command. Use 'add <number>' to add a number.");
                }
            }
            else
            {
                Console.WriteLine(
                    "Unknown command. Use 'add <number>' to add a number or 'print sum' to print the sum or 'exit' to exit.");
            }
        }
    }

    static (int, int) ReadInitialValues(string xmlFile)
    {
        XDocument doc = XDocument.Load(xmlFile);
        string? num1Str = doc.Root?.Element("Number1")?.Value;
        string? num2Str = doc.Root?.Element("Number2")?.Value;
		if (num1Str == null || num2Str == null)
		{
			throw new Exception("Invalid XML format");
        }
        int num1 = int.Parse(num1Str);
        int num2 = int.Parse(num2Str);
        return (num1, num2);
    }
}
