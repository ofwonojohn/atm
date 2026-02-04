#!/bin/bash
# ATM System - Quick Start Guide

echo "========================================"
echo "ATM System - Spring Boot Application"
echo "========================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Step 1: Build the project
echo -e "${BLUE}Step 1: Building the project...${NC}"
mvn clean install
if [ $? -ne 0 ]; then
    echo -e "${YELLOW}Build failed. Please check the error messages above.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Build successful!${NC}"
echo ""

# Step 2: Run the application
echo -e "${BLUE}Step 2: Starting the application...${NC}"
echo -e "${YELLOW}The application will be available at: http://localhost:8080${NC}"
echo ""
echo -e "${YELLOW}Test Credentials:${NC}"
echo "  Account 1: 1001 / PIN: 1234 (Balance: Rs. 50,000)"
echo "  Account 2: 1002 / PIN: 5678 (Balance: Rs. 75,000)"
echo "  Account 3: 1003 / PIN: 9012 (Balance: Rs. 1,00,000)"
echo ""
echo -e "${YELLOW}H2 Console: http://localhost:8080/h2-console${NC}"
echo -e "${YELLOW}Database: jdbc:h2:mem:atmdb${NC}"
echo ""

# Run the application
mvn spring-boot:run
