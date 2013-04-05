echo "Compiling .java files..."
javac -d . src/*.java
if [ $? -eq 0 ]; then
    echo "Running EDrumSimulator..."
    java com/adarwin/edrum/EDrumSimulator
fi
