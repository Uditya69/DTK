#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <stdexcept>
#include "pugixml.hpp"

int totalSum = 0;

void readInitialValues(const std::string& xmlFile, int& num1, int& num2) {
    pugi::xml_document doc;
    pugi::xml_parse_result result = doc.load_file(xmlFile.c_str());
    if (!result) {
        throw std::runtime_error("Error loading XML file");
    }

    pugi::xml_node root = doc.first_child();

    pugi::xml_node number1Node = root.child("Number1");
    pugi::xml_node number2Node = root.child("Number2");

    if (number1Node && number2Node) {
        num1 = number1Node.text().as_int();
        num2 = number2Node.text().as_int();
    } else {
        throw std::runtime_error("Number1 or Number2 nodes not found");
    }
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        std::cerr << "Usage: " << argv[0] << " <input.xml>" << std::endl;
        return 1;
    }

    std::string xmlFile = argv[1];
    int num1 = 0, num2 = 0;
    try {
        readInitialValues(xmlFile, num1, num2);
        totalSum = num1 + num2; // Ensure initial values are added to totalSum
    } catch (const std::exception& e) {
        std::cerr << "Error reading initial values: " << e.what() << std::endl;
        return 1;
    }

    std::string userInput;
    while (true) {
        std::getline(std::cin, userInput);

        if (userInput == "exit") {
            break;
        } else if (userInput == "print sum") {
            std::cout << totalSum << std::endl;
        } else if (userInput.rfind("add ", 0) == 0) {
            try {
                int number = std::stoi(userInput.substr(4));
                totalSum += number;
            } catch (const std::exception&) {
                std::cerr << "Invalid command. Use 'add <number>' to add a number." << std::endl;
            }
        } else {
            std::cerr << "Unknown command. Use 'add <number>' to add a number or 'print sum' to print the sum or 'exit' to exit." << std::endl;
        }
    }

    return 0;
}