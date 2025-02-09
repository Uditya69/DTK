import sys
import xml.etree.ElementTree as ET


def read_initial_values(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    num1 = int(root.find('Number1').text)
    num2 = int(root.find('Number2').text)
    return num1, num2

def main():
    if len(sys.argv) < 2:
        print("Usage: python Main.py <input.xml>")
        return

    xml_file = sys.argv[1]
    total_sum = 0
    try:
        num1, num2 = read_initial_values(xml_file)
        total_sum = num1 + num2
    except Exception as e:
        print(f"Error reading initial values: {e}")
        return

    while True:
        user_input = input().strip()

        if user_input.lower() == "exit":
            break
        elif user_input.lower() == "print sum":
            print(total_sum)
        elif user_input.lower().startswith("add "):
            try:
                number = int(user_input.split()[1])
                total_sum += number
            except (IndexError, ValueError):
                print("Invalid command. Use 'add <number>' to add a number to the sum.")
        else:
            print("Unknown command. Use 'add <number>' to add a number or 'print sum' to print the sum or 'exit' to exit.")

if __name__ == "__main__":
    main()