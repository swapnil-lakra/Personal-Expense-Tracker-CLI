# Personal Expense Tracker (CLI) 💰

A command-line based **Personal Expense Tracker** built using **Core Java** to practically demonstrate **OOPs concepts**, **Exception Handling**, and the **Collection Framework**.

Track your daily expenses, filter them, update/delete records, and generate useful reports — all without any external library or database. Data is stored in-memory during runtime.

---

## ✨ Features

- ➕ **Add Expense** — amount, category, date, and optional description
- 📋 **View All Expenses** — clean table format with total
- 🔍 **Search / Filter** — by category, date range, keyword, or amount range
- ✏️ **Update Expense** — update any field using expense ID
- 🗑️ **Delete Expense** — with confirmation prompt
- 📊 **Reports & Analytics**
  - Total spending
  - Category-wise breakdown (with percentage)
  - Monthly report
  - Date range report
  - Top 5 highest expenses
  - Average expense per transaction
- 🔃 **Sort Expenses** — by date / amount / category (asc/desc)
- 🛡️ **Robust Input Validation** — no crashes on invalid input

---

## 🧠 Concepts Practiced

### OOPs
- **Encapsulation** — private fields with getters/setters in `Expense`
- **Inheritance** — custom exceptions extending `Exception`
- **Abstraction** — `ExpenseManager` hides internal storage details
- **Polymorphism** — method overriding (`toString()`), interface-based comparison

### Exception Handling
- Custom **checked exceptions**: `InvalidAmountException`, `InvalidCategoryException`, `ExpenseNotFoundException`, `InvalidDateException`
- `try-catch` at UI boundary — business logic stays clean
- Input parsing errors wrapped into meaningful custom exceptions

### Collection Framework
| Requirement | Collection Used |
|---|---|
| Store all expenses | `ArrayList<Expense>` |
| Fast lookup by ID | `HashMap<Integer, Expense>` |
| Category-wise totals | `HashMap<Category, Double>` |
| Monthly report | `TreeMap<String, Double>` (sorted) |
| Top expenses / Sorting | `List` + `Comparator` |

---

## 🏗️ Project Structure

```
com.expensetracker
│
├── Main.java                    # Entry point & menu loop
│
├── model/
│   ├── Expense.java             # Expense entity
│   └── Category.java            # Category enum
│
├── service/
│   └── ExpenseManager.java      # Business logic + Collections
│
├── exception/
│   ├── InvalidAmountException.java
│   ├── InvalidCategoryException.java
│   ├── ExpenseNotFoundException.java
│   └── InvalidDateException.java
│
├── util/
│   ├── InputValidator.java      # Input validation
│   ├── DateUtil.java            # Date helpers
│   └── ConsoleUtil.java         # Formatting helpers
│
├── ui/
│   └── Menu.java                # All menu displays
│
└── constants/
    └── AppConstants.java        # App-wide constants
```

**Design:** Layered architecture — `model` (data), `service` (logic), `ui` (display), `util` (helpers), `Main` (wiring). Follows **Single Responsibility Principle**.

---

## 🚀 How to Run

### Prerequisites
- **Java 8 or higher**
- Any terminal / IDE (IntelliJ IDEA, Eclipse, VS Code)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/personal-expense-tracker.git
   cd personal-expense-tracker
   ```

2. **Compile all Java files**
   ```bash
   javac -d out src/com/expensetracker/**/*.java
   ```

3. **Run the application**
   ```bash
   java -cp out com.expensetracker.Main
   ```

   **Or if using an IDE** — just open the project and run `Main.java`.

---

## 🖥️ Application Screenshots

Explore the application's console interface and see how its core features work through the screenshots below.

### Main Menu
![Main Menu](screenshots/Main%20menu.png)

### Expense Management

**Add Expense**

![Add Expense](screenshots/Add%20expense.png)

**View All Expenses**

![All Expenses](screenshots/All%20expenses.png)

**Delete Expense**

![Delete Expense](screenshots/Delete%20expense.png)

### Additional Features

The following folders contain screenshots demonstrating the application's remaining functionality:

- [Search & Filter Expenses](screenshots/Search%20%7C%20Filter%20expenses/)
- [Update Expenses](screenshots/Update%20Expenses/)
- [Sort Expenses](screenshots/Sort%20Expenses/)
- [Reports & Analytics](screenshots/Reports%20and%20Analytics/)

### Exit Application

![Exit Application](screenshots/Exit.png)

---

## 🖥️ Sample Usage

```
=========================================
     PERSONAL EXPENSE TRACKER v1.0
=========================================
1. Add Expense
2. View All Expenses
3. Search / Filter Expenses
4. Update Expense
5. Delete Expense
6. Reports & Analytics
7. Sort Expenses
8. Exit
=========================================
Enter your choice: 1

--- ADD NEW EXPENSE ---
Enter amount (Rs.): 250.50
Select category:
   1. FOOD
   2. TRAVEL
   3. SHOPPING
   4. BILLS
   5. ENTERTAINMENT
   6. HEALTH
   7. OTHER
Enter category choice: 1
Enter description (optional, max 100 chars): Lunch at office
Enter date (dd-MM-yyyy) [Enter = today]:

[OK] Expense added successfully! (ID: 1001)
```

---

## ⚙️ Validation Rules

- Amount must be `> 0` and `<= 10,00,000`
- Description ≤ 100 characters (optional)
- Date format: `dd-MM-yyyy`, future dates not allowed
- Menu choices must be within range
- Update/Delete requires an existing expense ID

---

## 📌 Roadmap (Future Enhancements)

- [ ] File persistence using **Serialization** / CSV
- [ ] Budget per category with warning on exceed
- [ ] JUnit test coverage
- [ ] Java Swing GUI version
- [ ] Export reports to PDF/Excel
- [ ] Design patterns (Strategy for sorting, Factory for categories)

---

## 🎯 Purpose

This project is part of my **Core Java learning journey** — an attempt to solidify `OOPs`, `Exception Handling`, and `Collection Framework` by solving a small real-world problem. Feedback and suggestions are welcome!

---

## 📄 License

This project is open-sourced under the **MIT License** — feel free to use and learn from it.

---

## 🙌 Connect

- **Author:** Swapnil Lakra
- **LinkedIn:** [swapnil-lakra](https://linkedin.com/in/swapnil-lakra)
- **Email:** swapnillakra822@gmail.com

---

⭐ If you found this project helpful or interesting, consider giving it a star!