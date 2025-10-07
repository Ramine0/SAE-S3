public class Table
{
    public int number;
    public Student student;

    public Table(int number, Student student)
    {
        this.number = number;
        this.student = student;
    }

    public void show()
    {
        if (student == null)
            System.out.print(" --- ");
        else
//        System.out.println(student.number + " " + student.name + " " + student.surname + " " + student.group + " " + student.subgroup);
            System.out.print(student.name + " ");
    }
}
