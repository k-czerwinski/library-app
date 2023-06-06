#BOOK INSERT
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (1, 'J.R.R. Tolkien', 'FANTASY', 1954, 'The Lord of the Rings');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (2, 'Jane Austen', 'NOVEL', 1813, 'Pride and Prejudice');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (3, 'William Shakespeare', 'POETRY', 1995, 'Romeo and Juliet');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (4, 'George Orwell', 'NONFICTION', 1949, '1984');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (5, 'J.K. Rowling', 'FANTASY', 1997, 'Harry Potter and the Philosophers Stone');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (6, 'Leo Tolstoy', 'NOVEL', 1869, 'War and Peace');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (7, 'Emily Dickinson', 'POETRY', 1890, 'The Complete Poems');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (8, 'Malcolm Gladwell', 'NONFICTION', 2008, 'Outliers');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (9, 'C.S. Lewis', 'FANTASY', 1950, 'The Lion, the Witch, and the Wardrobe');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (10, 'Markus Zusak', 'NOVEL', 2005, 'The Book Thief');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (11, 'Robert Frost', 'POETRY', 1916, 'Mountain Interval');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (12, 'Yuval Noah Harari', 'NONFICTION', 2014, 'Sapiens: A Brief History of Humankind');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (13, 'Philip Pullman', 'FANTASY', 1995, 'Northern Lights');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (14, 'Ernest Hemingway', 'NOVEL', 1926, 'The Sun Also Rises');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (15, 'Maya Angelou', 'POETRY', 1969, 'I Know Why the Caged Bird Sings');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (16, 'Bill Bryson', 'NONFICTION', 2003, 'A Short History of Nearly Everything');
INSERT INTO books(book_id, author, book_genre, publication_year, title) VALUES (17, 'Terry Pratchett', 'FANTASY', 1983, 'The Colour of Magic');
#BOOK COLLECTION INSERT
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,1);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (30,30,2);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (45,45,3);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (60,60,4);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (10,10,5);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (1,1,6);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (2,2,7);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (25,25,8);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (2,2,9);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,10);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,11);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,12);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,13);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,14);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,15);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,16);
INSERT INTO books_collection(current_amount,total_amount,book_id) VALUES (20,20,17);
# USER INSERT, password to each account: 123Password##
INSERT INTO users(user_id,email,name,surname,password, role) VALUES (1, 'user@user.com', 'Customer','XXX','$2a$10$4PyfscAMVhZZsVd7iRP1j.hOK/zyyCjJb/5WS3lXB.xPNkttU19A2',0);
INSERT INTO users(user_id,email,name,surname,password, role) VALUES (2, 'admin@admin.com', 'Manager','YYY','$2a$10$4PyfscAMVhZZsVd7iRP1j.hOK/zyyCjJb/5WS3lXB.xPNkttU19A2',1);