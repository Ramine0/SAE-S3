private static final HashMap<Integer, Table> tables = new HashMap<>();
private static final Set<Integer> constraints = new HashSet<>();

void main()
{
    Student neo = new Student(2403367, "Néo", "Encore Moi", "G2", "G2.1");
    Student malik = new Student(2406410, "Malik", "Le Grand", "G2", "G2.1");
    Student victor = new Student(2403918, "Victor", "DChips", "G2", "G2.1");
    Student felix = new Student(24000000, "Félix", "qu'un", "G2", "G2.2");
    Student walid = new Student(24000001, "Walid", "qu'un d'autre", "G1", "G1.2");

    constraints.add(malik.number);
    constraints.add(victor.number);
    constraints.add(neo.number);

    Student[] students = {neo, malik, victor, felix, walid};

    for (int i = 0; i < 5; i++)
        tables.put(i, new Table(i, null));

    placeStudents(students);

    show();
}

private static void placeStudents(Student... students)
{
    Collections.shuffle(Arrays.asList(students));

    for (Student student : students)
        for (int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);

            if (table != null && table.student == null)
            {
                if (isValidPlacement(i, student))
                {
                    table.student = student;
                }
                else
                {

                }
                break;
            }
        }
}

private static boolean isValidPlacement(int tableNumber, Student student)
{
    if (tableNumber > 0)
    {
        Student prevStudent = tables.get(tableNumber - 1).student;
        if (prevStudent != null && constraints.contains(prevStudent.number) && constraints.contains(student.number))
        {
            return false;
        }
    }

    if (tableNumber < tables.size() - 1)
    {
        Student nextStudent = tables.get(tableNumber + 1).student;
        if (nextStudent != null && constraints.contains(nextStudent.number) && constraints.contains(student.number))
        {
            return false;
        }
    }

    return true;
}

private static void show()
{
    IO.println();
    IO.println("------------------------------------");

    for (Table t : tables.values())
        t.show();
}
