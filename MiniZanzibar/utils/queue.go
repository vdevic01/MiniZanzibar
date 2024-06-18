package utils

import "errors"

// Queue represents a queue data structure
type Queue[T any] struct {
	items []T
}

// NewQueue creates a new queue
func NewQueue[T any]() *Queue[T] {
	return &Queue[T]{items: []T{}}
}

// Enqueue adds an item to the end of the queue
func (q *Queue[T]) Enqueue(item T) {
	q.items = append(q.items, item)
}

// Dequeue removes and returns the item from the front of the queue
func (q *Queue[T]) Dequeue() (T, error) {
	if q.IsEmpty() {
		return *new(T), errors.New("queue is empty")
	}
	item := q.items[0]
	q.items = q.items[1:]
	return item, nil
}

// Peek returns the item at the front of the queue without removing it
func (q *Queue[T]) Peek() (T, error) {
	if q.IsEmpty() {
		return *new(T), errors.New("queue is empty")
	}
	return q.items[0], nil
}

// IsEmpty checks if the queue is empty
func (q *Queue[T]) IsEmpty() bool {
	return len(q.items) == 0
}

// Size returns the number of items in the queue
func (q *Queue[T]) Size() int {
	return len(q.items)
}
