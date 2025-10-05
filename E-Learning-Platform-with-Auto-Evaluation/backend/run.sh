#!/bin/bash

echo "=== E-Learning Platform Runner ==="
echo "Setting up manual compilation..."

# Create lib directory for dependencies
mkdir -p lib

# Note: You need to download these JAR files manually:
echo "REQUIRED JAR FILES (download to lib/ folder):"
echo "1. aws-java-sdk-dynamodb-1.12.470.jar"
echo "2. aws-java-sdk-s3-1.12.470.jar"
echo "3. aws-core-1.12.470.jar"
echo "4. commons-fileupload-1.4.jar"
echo "5. poi-5.2.0.jar"
echo "6. poi-ooxml-5.2.0.jar"
echo "7. pdfbox-2.0.24.jar"
echo "8. jackson-core-2.13.0.jar"
echo "9. jackson-databind-2.13.0.jar"
echo "10. jetty-server-9.4.49.v20220914.jar"
echo "11. jetty-servlet-9.4.49.v20220914.jar"
echo "12. servlet-api-3.1.0.jar"
echo ""

# Check if lib directory has files
if [ "$(ls -A lib/)" ]; then
    echo "Found JAR files in lib/, proceeding with compilation..."

    # Compile Java files
    echo "Compiling Java files..."
    javac -cp "lib/*" -d target/classes src/main/java/com/elearning/app/*.java

    if [ $? -eq 0 ]; then
        echo "Compilation successful!"
        echo "Starting E-Learning Platform..."
        java -cp "target/classes:lib/*" com.elearning.app.ELearningApp
    else
        echo "Compilation failed!"
    fi
else
    echo "Please download the required JAR files to lib/ directory first"
    echo "Or install Maven: sudo apt install maven"
fi