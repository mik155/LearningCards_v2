package database;

public enum Mode
{
    CREATE
    {
        public boolean open()
        {
            return false;
        }

        public boolean create()
        {
            return true;
        }
    },

    OPEN
    {
        public boolean open()
        {
            return true;
        }

        public boolean create()
        {
            return false;
        }
    };

    public abstract boolean open();
    public abstract boolean create();
}
