package application;

/**
 * Created by cal13 on 3/22/2017.
 */
public abstract class Person
{
    private String name;
    private String username;
    private String password;

    public String getName()
    {
        return this.name;
    }

    public boolean checkPassword(String password)
    {
        return (password.compareTo(this.password) == 0);
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
