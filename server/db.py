import sqlite3
import threading
import logging


class MRBSDb:
    def __init__(self):
        self.__db_path = "mrbs.db"
        self._log = logging.getLogger()

        self.__db_lock = threading.RLock()
        self.__connection = sqlite3.connect(self.__db_path, check_same_thread=False)

        self._log.info("Database {} was created".format(self.__db_path))

    def __del__(self):
        if self.__connection:
            self.__connection.close()
            self.__connection = None

    def insert_booking(self, title, year, month, day, start_hour, start_min, end_hour, end_min, room):
        return self.__execute_query("insert into Bookings(Title, Year, Month, Day, StartHour, StartMin, EndHour, EndMin,"
                                    "Room) values(?, ?, ?, ?, ?, ?, ?, ?, ?)", (title, year, month, day, start_hour,
                                                                                start_min, end_hour, end_min, room))[0]

    def update_booking(self, id, title, year, month, day, start_hour, start_min, end_hour, end_min, room):
        return self.__execute_query("update Bookings set Title=?, Year=?, Month=?, Day=?, StartHour=?, StartMin=?, "
                                    "EndHour=?, EndMin=?, Room=? where Id=?", (title, year, month, day, start_hour,
                                                                               start_min, end_hour, end_min, room, id))[1]

    def delete_booking(self, id):
        return self.__execute_query("delete from Bookings where id=?", (id,))[1]

    def get_latest_bookings(self, last_booking_id):
        return self.__run_query_fetch_all("select * from Bookings where Id > ?", (last_booking_id,))

    def get_bookings_for_day_and_room(self, year, month, day, room):
        return self.__run_query_fetch_all("select * from Bookings where Year=? and Month=? and Day=? and Room=?",
                                          (year, month, day, room,))

    def __execute_query(self, query, params=()):
        c = None
        self.__db_lock.acquire()

        try:
            c = self.__connection.cursor()

            c.execute(query, params)
            self._log.debug("Executed query \"{}\" with params: {}".format(query, params))

            self.__connection.commit()

            last_row_id = c.lastrowid
            row_count = c.rowcount

            c.close()
            c = None

            return last_row_id, row_count
        finally:
            if c:
                c.close()
            self.__db_lock.release()

    def __run_query_fetch_all(self, query, params=()):
        c = None
        self.__db_lock.acquire()

        try:
            c = self.__connection.cursor()

            ret_val = c.execute(query, params).fetchall()
            self._log.debug("Executed query \"{}\" with params: {}".format(query, params))

            self.__connection.commit()

            c.close()
            c = None

            return ret_val

        finally:
            if c:
                c.close()
            self.__db_lock.release()
