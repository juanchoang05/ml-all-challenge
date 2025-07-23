#!/bin/bash

# JaCoCo Coverage Analysis Script for MercadoLibre Core Platform
# This script provides easy commands to run coverage analysis

echo "🚀 JaCoCo Coverage Analysis Tool"
echo "================================="

# Function to show help
show_help() {
    echo ""
    echo "Usage: ./jacoco-coverage.sh [OPTION]"
    echo ""
    echo "Options:"
    echo "  test         Run all tests with coverage"
    echo "  test-unit    Run only unit tests with coverage"
    echo "  test-class   Run specific test class (requires class name)"
    echo "  report       Generate coverage report only"
    echo "  check        Check coverage thresholds"
    echo "  verify       Run tests and verify coverage"
    echo "  open         Open HTML coverage report in browser"
    echo "  clean        Clean previous coverage data"
    echo "  help         Show this help message"
    echo ""
    echo "Examples:"
    echo "  ./jacoco-coverage.sh test"
    echo "  ./jacoco-coverage.sh test-class PaymentControllerTest"
    echo "  ./jacoco-coverage.sh open"
    echo ""
}

# Function to clean previous coverage data
clean_coverage() {
    echo "🧹 Cleaning previous coverage data..."
    mvn clean
    echo "✅ Coverage data cleaned"
}

# Function to run all tests with coverage
run_all_tests() {
    echo "🧪 Running all tests with JaCoCo coverage..."
    mvn clean test
    if [ $? -eq 0 ]; then
        echo "✅ Tests completed successfully"
        echo "📊 Coverage report generated at: target/site/jacoco/index.html"
    else
        echo "❌ Tests failed"
        exit 1
    fi
}

# Function to run specific test class
run_test_class() {
    if [ -z "$2" ]; then
        echo "❌ Error: Test class name required"
        echo "Usage: ./jacoco-coverage.sh test-class <TestClassName>"
        exit 1
    fi
    
    echo "🧪 Running test class: $2"
    mvn clean test -Dtest="$2"
    if [ $? -eq 0 ]; then
        echo "✅ Test completed successfully"
        echo "📊 Coverage report generated at: target/site/jacoco/index.html"
    else
        echo "❌ Test failed"
        exit 1
    fi
}

# Function to generate report only
generate_report() {
    echo "📊 Generating JaCoCo coverage report..."
    mvn jacoco:report
    if [ $? -eq 0 ]; then
        echo "✅ Coverage report generated successfully"
        echo "📁 Report location: target/site/jacoco/index.html"
    else
        echo "❌ Report generation failed"
        exit 1
    fi
}

# Function to check coverage thresholds
check_coverage() {
    echo "📏 Checking coverage thresholds..."
    mvn jacoco:check
    if [ $? -eq 0 ]; then
        echo "✅ Coverage thresholds met"
    else
        echo "❌ Coverage thresholds not met"
        echo "📊 Check the report for detailed coverage information"
        exit 1
    fi
}

# Function to run tests and verify coverage
verify_coverage() {
    echo "🔍 Running tests and verifying coverage..."
    mvn clean verify
    if [ $? -eq 0 ]; then
        echo "✅ Tests and coverage verification completed successfully"
        echo "📊 Coverage report generated at: target/site/jacoco/index.html"
    else
        echo "❌ Tests or coverage verification failed"
        exit 1
    fi
}

# Function to open HTML report
open_report() {
    local report_file="target/site/jacoco/index.html"
    
    if [ ! -f "$report_file" ]; then
        echo "❌ Coverage report not found. Run tests first to generate the report."
        echo "💡 Try: ./jacoco-coverage.sh test"
        exit 1
    fi
    
    echo "🌐 Opening coverage report in browser..."
    
    # Detect OS and open accordingly
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open "$report_file"
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        xdg-open "$report_file"
    elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        # Windows
        start "$report_file"
    else
        echo "📁 Report available at: $(pwd)/$report_file"
    fi
}

# Function to show coverage summary
show_summary() {
    local report_file="target/site/jacoco/jacoco.csv"
    
    if [ ! -f "$report_file" ]; then
        echo "❌ Coverage summary not available. Run tests first."
        return 1
    fi
    
    echo ""
    echo "📊 Coverage Summary:"
    echo "==================="
    
    # Parse CSV and show summary (simplified)
    tail -n +2 "$report_file" | while IFS=, read -r group package class instruction_missed instruction_covered branch_missed branch_covered line_missed line_covered complexity_missed complexity_covered method_missed method_covered
    do
        if [ "$group" = "ml-core-platform" ]; then
            total_instructions=$((instruction_missed + instruction_covered))
            total_branches=$((branch_missed + branch_covered))
            total_lines=$((line_missed + line_covered))
            
            if [ $total_instructions -gt 0 ]; then
                instruction_coverage=$(echo "scale=1; $instruction_covered * 100 / $total_instructions" | bc)
                echo "Instruction Coverage: ${instruction_coverage}%"
            fi
            
            if [ $total_branches -gt 0 ]; then
                branch_coverage=$(echo "scale=1; $branch_covered * 100 / $total_branches" | bc)
                echo "Branch Coverage: ${branch_coverage}%"
            fi
            
            if [ $total_lines -gt 0 ]; then
                line_coverage=$(echo "scale=1; $line_covered * 100 / $total_lines" | bc)
                echo "Line Coverage: ${line_coverage}%"
            fi
            
            break
        fi
    done
    echo ""
}

# Main script logic
case "${1:-help}" in
    "test")
        run_all_tests
        show_summary
        ;;
    "test-unit")
        echo "🧪 Running unit tests with coverage..."
        mvn clean test
        show_summary
        ;;
    "test-class")
        run_test_class "$@"
        show_summary
        ;;
    "report")
        generate_report
        ;;
    "check")
        check_coverage
        ;;
    "verify")
        verify_coverage
        show_summary
        ;;
    "open")
        open_report
        ;;
    "clean")
        clean_coverage
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        echo "❌ Unknown option: $1"
        show_help
        exit 1
        ;;
esac
